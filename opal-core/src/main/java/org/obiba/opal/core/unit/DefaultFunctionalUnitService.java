/*******************************************************************************
 * Copyright 2008(c) The OBiBa Consortium. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.obiba.opal.core.unit;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.obiba.opal.core.cfg.OpalConfiguration;
import org.obiba.opal.core.cfg.OpalConfigurationService;
import org.obiba.opal.core.cfg.OpalConfigurationService.ConfigModificationTask;
import org.obiba.opal.core.runtime.OpalRuntime;
import org.obiba.opal.core.service.NoSuchFunctionalUnitException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
/**
 * An implementation of {@link FunctionalUnitService} on top of {@link OpalConfiguration}.
 */
public class DefaultFunctionalUnitService implements FunctionalUnitService {

  private final OpalConfigurationService configService;

  private final ApplicationContext applicationContext;

  @Autowired
  public DefaultFunctionalUnitService(ApplicationContext applicationContext, OpalConfigurationService configService) {
    this.configService = configService;
    this.applicationContext = applicationContext;
  }

  public boolean hasFunctionalUnit(String unitName) {
    return getFunctionalUnit(unitName) != null;
  }

  public void removeFunctionalUnit(final String unitName) {
    configService.modifyConfiguration(new ConfigModificationTask() {

      @Override
      public void doWithConfig(OpalConfiguration config) {
        FunctionalUnit unit = getFunctionalUnit(unitName);
        if(unit != null) {
          config.getFunctionalUnits().remove(unit);
        }
      }
    });
  }

  public FunctionalUnit getFunctionalUnit(String unitName) {
    for(FunctionalUnit unit : getConfiguredFunctionalUnits()) {
      if(unit.getName().equals(unitName)) {
        return unit;
      }
    }
    return null;
  }

  public Set<FunctionalUnit> getFunctionalUnits() {
    return Collections.unmodifiableSet(getConfiguredFunctionalUnits());
  }

  public void addOrReplaceFunctionalUnit(final FunctionalUnit functionalUnit) {
    configService.modifyConfiguration(new ConfigModificationTask() {

      @Override
      public void doWithConfig(OpalConfiguration config) {
        FunctionalUnit unit = getFunctionalUnit(functionalUnit.getName());
        if(unit != null) config.getFunctionalUnits().remove(unit);
        config.getFunctionalUnits().add(functionalUnit);
      }
    });
  }

  @Override
  public FileObject getUnitDirectory(String unitName) throws NoSuchFunctionalUnitException, FileSystemException {
    if(hasFunctionalUnit(unitName) == false) {
      throw new NoSuchFunctionalUnitException(unitName);
    }

    FileObject unitsDir = getOpalRuntime().getFileSystem().getRoot().resolveFile("units");
    unitsDir.createFolder();

    FileObject unitDir = unitsDir.resolveFile(unitName);
    unitDir.createFolder();

    return unitDir;
  }

  private Set<FunctionalUnit> getConfiguredFunctionalUnits() {
    return configService.getOpalConfiguration().getFunctionalUnits();
  }

  // We get the OpalRuntime this way in order to remove the circular dependency:
  // DefaultOpalRuntime -> OpalJettyServer -> SecurityManager -> FunctionalUnitRealm -> FunctionalUnitService ->
  // DefaultOpalRuntime
  private OpalRuntime getOpalRuntime() {
    return this.applicationContext.getBean(OpalRuntime.class);
  }

}
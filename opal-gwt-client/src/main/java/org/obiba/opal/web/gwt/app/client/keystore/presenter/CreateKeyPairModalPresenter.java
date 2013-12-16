/*
 * Copyright (c) 2013 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.obiba.opal.web.gwt.app.client.keystore.presenter;

import java.util.LinkedHashSet;
import java.util.Set;

import org.obiba.opal.web.gwt.app.client.keystore.support.KeystoreType;
import org.obiba.opal.web.gwt.app.client.presenter.ModalPresenterWidget;
import org.obiba.opal.web.gwt.app.client.validator.FieldValidator;
import org.obiba.opal.web.gwt.app.client.validator.RequiredTextValidator;
import org.obiba.opal.web.gwt.app.client.validator.ViewValidationHandler;

import com.google.gwt.user.client.ui.HasText;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PopupView;

public class CreateKeyPairModalPresenter extends ModalPresenterWidget<CreateKeyPairModalPresenter.Display>
    implements KeyPairModalUiHandlers {

  private SaveHandler saveHandler;

  private KeystoreType keystoreType;

  @Inject
  public CreateKeyPairModalPresenter(Display display, EventBus eventBus) {
    super(eventBus, display);
    getView().setUiHandlers(this);
  }

  @Override
  public void save() {
    getView().clearErrors();
    if(new ViewValidator().validate()) {
      if(saveHandler != null) {
        saveHandler
            .save(getView().getName().getText(), getView().getAlgorithm().getText(), getView().getSize().getText(),
                getView().getFirstLastName().getText(), getView().getOrganizationalUnit().getText(),
                getView().getOrganizationalUnit().getText(), getView().getLocality().getText(),
                getView().getState().getText(), getView().getCountry().getText());
      }
      getView().close();
    }
  }

  public void initialize(KeystoreType type, SaveHandler handler) {
    saveHandler = handler;
    keystoreType = type;
    getView().setType(type);
  }

  private final class ViewValidator extends ViewValidationHandler {

    private ViewValidator() {}

    @Override
    protected Set<FieldValidator> getValidators() {
      Set<FieldValidator> validators = new LinkedHashSet<FieldValidator>();

      if (keystoreType == KeystoreType.PROJECT) {
        validators.add(new RequiredTextValidator(getView().getName(), "KeyPairAliasIsRequired",
            Display.FormField.NAME.name()));
      }

      validators.add(new RequiredTextValidator(getView().getAlgorithm(), "KeyPairAlgorithmIsRequired",
          Display.FormField.ALGORITHM.name()));
      validators.add(
          new RequiredTextValidator(getView().getSize(), "KeyPairKeySizeIsRequired", Display.FormField.SIZE.name()));
      validators.add(new RequiredTextValidator(getView().getFirstLastName(), "KeyPairFirstAndLastNameIsRequired",
          Display.FormField.FIRST_LAST_NAME.name()));
      validators.add(new RequiredTextValidator(getView().getOrganizationalUnit(), "KeyPairOrganizationalUnitIsRequired",
          Display.FormField.ORGANIZATIONAL_UNIT.name()));
      return validators;
    }

    @Override
    protected void showMessage(String id, String message) {
      getView().showError(Display.FormField.valueOf(id), message);
    }
  }

  public interface SaveHandler {
    void save(String alias, String algorithm, String size, String firstLastName, String organization, String organizationalUnit,
        String locality, String state, String country);
  }

  public interface Display extends KeyPairDisplay<Display.FormField>, PopupView, HasUiHandlers<KeyPairModalUiHandlers> {

    enum FormField {
      NAME,
      ALGORITHM,
      SIZE,
      FIRST_LAST_NAME,
      ORGANIZATIONAL_UNIT
    }

    HasText getName();

    HasText getAlgorithm();

    HasText getSize();

    HasText getFirstLastName();

    HasText getOrganization();

    HasText getOrganizationalUnit();

    HasText getLocality();

    HasText getState();

    HasText getCountry();

    void setType(KeystoreType type);
  }

}
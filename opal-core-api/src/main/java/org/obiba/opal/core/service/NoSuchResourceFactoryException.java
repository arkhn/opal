/*
 * Copyright (c) 2020 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.obiba.opal.core.service;

public class NoSuchResourceFactoryException extends RuntimeException {

  private final String provider;

  private final String name;

  public NoSuchResourceFactoryException(String provider, String name) {
    super("No Resource factory with name \"" + name +"\" in provider \"" + provider + "\"");
    this.provider = provider;
    this.name = name;
  }

  public String getProvider() {
    return provider;
  }

  public String getName() {
    return name;
  }
}
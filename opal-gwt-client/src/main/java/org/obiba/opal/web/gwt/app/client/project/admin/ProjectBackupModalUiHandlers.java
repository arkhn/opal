/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.obiba.opal.web.gwt.app.client.project.admin;

import com.gwtplatform.mvp.client.UiHandlers;
import org.obiba.opal.web.gwt.app.client.ui.ModalUiHandlers;

public interface ProjectBackupModalUiHandlers extends UiHandlers, ModalUiHandlers {
  void onBackup(boolean viewsAsTables);
}

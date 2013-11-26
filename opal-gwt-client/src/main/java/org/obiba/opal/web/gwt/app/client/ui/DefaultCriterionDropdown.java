/*
 * Copyright (c) 2013 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.obiba.opal.web.gwt.app.client.ui;

import org.obiba.opal.web.gwt.app.client.i18n.Translations;
import org.obiba.opal.web.model.client.magma.VariableDto;

import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.watopi.chosen.client.event.ChosenChangeEvent;

public abstract class DefaultCriterionDropdown extends CriterionDropdown {
  private static final Translations translations = GWT.create(Translations.class);

  private Chooser operatorChooser;

  private TextBox matches;

  public DefaultCriterionDropdown(VariableDto variableDto, String fieldName) {
    super(variableDto, fieldName, null);
  }

  @Override
  public Widget getSpecificControls() {
    ListItem specificControls = new ListItem();

    operatorChooser = new Chooser();
    matches = new TextBox();
    specificControls.addStyleName("controls");

    specificControls.add(getOperatorsChooserPanel());

    matches.setPlaceholder(translations.criterionFiltersMap().get("custom_match_query"));
    matches.addFocusHandler(new FocusHandler() {
      @Override
      public void onFocus(FocusEvent event) {
        resetRadioControls();
      }
    });
    matches.addKeyUpHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(KeyUpEvent event) {
        updateMatchCriteriaFilter();
      }
    });

    specificControls.add(matches);
    return specificControls;
  }

  private SimplePanel getOperatorsChooserPanel() {
    SimplePanel inPanel = new SimplePanel();
    operatorChooser.addItem(translations.criterionFiltersMap().get("like"));
    operatorChooser.addFocusHandler(new FocusHandler() {
      @Override
      public void onFocus(FocusEvent event) {
        resetRadioControls();
      }
    });
    operatorChooser.addChosenChangeHandler(new UpdateFilterChosenHandler());

    inPanel.add(operatorChooser);
    return inPanel;
  }

  @Override
  public void resetSpecificControls() {
    operatorChooser.setItemSelected(0, true);
  }

  @Override
  public String getQueryString() {
    String emptyNotEmpty = super.getQueryString();
    if(emptyNotEmpty != null) return emptyNotEmpty;

    return "";
  }

  private class UpdateFilterChosenHandler implements ChosenChangeEvent.ChosenChangeHandler {
    @Override
    public void onChange(ChosenChangeEvent chosenChangeEvent) {
      resetRadioControls();
      updateMatchCriteriaFilter();
    }
  }

  private void updateMatchCriteriaFilter() {
    if(matches.getText().isEmpty()) {
      updateCriterionFilter("");
    } else {
      updateCriterionFilter(translations.criterionFiltersMap().get("like") + " " + matches.getText());
      doFilterValueSets();
    }
  }
}
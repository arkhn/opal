/*******************************************************************************
 * Copyright 2008(c) The OBiBa Consortium. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.obiba.opal.web.gwt.app.client.widgets.presenter;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.place.Place;
import net.customware.gwt.presenter.client.place.PlaceRequest;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

import org.obiba.opal.web.gwt.rest.client.ResourceCallback;
import org.obiba.opal.web.gwt.rest.client.ResourceRequestBuilderFactory;
import org.obiba.opal.web.model.client.magma.DatasourceDto;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.Response;
import com.google.inject.Inject;

/**
 * Widget for selecting an Opal datasource.
 */
public class DatasourceSelectorPresenter extends WidgetPresenter<DatasourceSelectorPresenter.Display> {
  //
  // Instance Variables
  //

  private DatasourcesRefreshedCallback datasourcesRefreshedCallback;

  //
  // Constructors
  //

  @Inject
  public DatasourceSelectorPresenter(final Display display, final EventBus eventBus) {
    super(display, eventBus);
  }

  //
  // WidgetPresenter Methods
  //

  @Override
  protected void onBind() {
  }

  @Override
  protected void onUnbind() {
  }

  @Override
  public void revealDisplay() {
  }

  @Override
  public void refreshDisplay() {
    ResourceRequestBuilderFactory.<JsArray<DatasourceDto>> newBuilder().forResource("/datasources").get().withCallback(new ResourceCallback<JsArray<DatasourceDto>>() {

      public void onResource(Response response, JsArray<DatasourceDto> resource) {
        JsArray<DatasourceDto> datasources = resource != null ? resource : (JsArray<DatasourceDto>) JsArray.createArray();
        getDisplay().setDatasources(datasources);

        if(datasourcesRefreshedCallback != null) {
          datasourcesRefreshedCallback.onDatasourcesRefreshed();
        }
      }
    }).send();
  }

  @Override
  public Place getPlace() {
    return null;
  }

  @Override
  protected void onPlaceRequest(PlaceRequest request) {
  }

  //
  // Methods
  //

  public String getSelection() {
    return getDisplay().getSelection();
  }

  public DatasourceDto getSelectionDto() {
    return getDisplay().getSelectionDto();
  }

  public void setSelection(String datasourceName) {
    getDisplay().setSelection(datasourceName);
  }

  public void setDatasourcesRefreshedCallback(DatasourcesRefreshedCallback datasourcesRefreshedCallback) {
    this.datasourcesRefreshedCallback = datasourcesRefreshedCallback;
  }

  //
  // Inner Classes / Interfaces
  //

  public interface Display extends WidgetDisplay {

    void setEnabled(boolean enabled);

    void setDatasources(JsArray<DatasourceDto> datasources);

    void selectFirst();

    void setSelection(String datasourceName);

    String getSelection();

    DatasourceDto getSelectionDto();
  }

  public interface DatasourcesRefreshedCallback {

    public void onDatasourcesRefreshed();
  }
}
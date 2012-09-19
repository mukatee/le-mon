/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.availabilitypage;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import osmo.common.log.Logger;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

//import fi.vtt.noen.mfw.bundle.server.shared.datamodel.ProbeDescription;

public class ProbeListDataProvider extends SortableDataProvider<ProbeDesc> {
  private final static Logger log = new Logger(ProbeListDataProvider.class);

  private final List<ProbeDesc> probes;

  public ProbeListDataProvider(List<ProbeDesc> probes) {
    this.probes = probes;
    setSort("name", true);
  }

  public Iterator<ProbeDesc> iterator(int i, int i1) {
    SortParam sp = getSort();
    String key = sp.getProperty();
    log.debug("sort key:" + key);
    if (sp.isAscending()) {
      Collections.sort(probes, new ProbeComparator(key, true));
    } else {
      Collections.sort(probes, new ProbeComparator(key, false));
    }
    return probes.subList(i, i + i1).iterator();
  }

  public int size() {
    return probes.size();
  }

  public IModel<ProbeDesc> model(ProbeDesc probeDesc) {
    return new DetachableProbeModel(probeDesc);
  }
}

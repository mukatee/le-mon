/*
 * Copyright (C) 2010-2011 VTT Technical Research Centre of Finland.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package fi.vtt.lemon.unittests.system.testprobes;

import fi.vtt.lemon.probe.Probe;

/**
 * @author Teemu Kanstren
 */
public class HangingProbe implements Probe {
  @Override
  public int getPrecision() {
    return 0;
  }

  @Override
  public String getMeasureURI() {
    return "This-one-hangs";
  }

  public String measure() {
    try {
      Thread.sleep(20000);
    } catch (InterruptedException e) {
    }
    return null;
  }
}

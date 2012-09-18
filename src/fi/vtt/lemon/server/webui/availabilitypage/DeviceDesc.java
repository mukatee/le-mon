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

package fi.vtt.lemon.server.webui.availabilitypage;

public class DeviceDesc {
  private final long deviceId;
  private final String name;
  private final String type;
  private final boolean disabled;
  
  public DeviceDesc(long deviceId, String name, String type, boolean disabled) {
    super();
    this.deviceId = deviceId;
    this.name = name;
    this.type = type;
    this.disabled = disabled;
  }

  public long getDeviceId() {
    return deviceId;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public boolean isDisabled() {
    return disabled;
  }
}

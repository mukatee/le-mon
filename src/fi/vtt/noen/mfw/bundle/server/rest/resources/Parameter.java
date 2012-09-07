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

package fi.vtt.noen.mfw.bundle.server.rest.resources;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="parameter")
@XmlType(propOrder = {"key", "value"})
public class Parameter
{
  private String key;
  private String value;

  public Parameter(){}
  
  public Parameter( String key, String value )
  {
    this.key = key;
    this.value = value;
  }

  @XmlElement
  public String getKey()
  {
    return key;
  }

  @XmlElement
  public String getValue()
  {
    return value;
  }
  
  public void setKey( String key )
  {
    this.key = key;
  }
  
  public void setValue( String value )
  {
    this.value = value;
  }
}

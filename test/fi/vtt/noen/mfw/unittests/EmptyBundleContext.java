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

package fi.vtt.noen.mfw.unittests;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Dictionary;

/**
 * Used in tests to create a fake bundlecontext to avoid nullpointers.
 *
 * @author Teemu Kanstren
 */
public class EmptyBundleContext implements BundleContext {
  @Override
  public String getProperty(String s) {
    return null;
  }

  @Override
  public Bundle getBundle() {
    return null;
  }

  @Override
  public Bundle installBundle(String s, InputStream inputStream) throws BundleException {
    return null;
  }

  @Override
  public Bundle installBundle(String s) throws BundleException {
    return null;
  }

  @Override
  public Bundle getBundle(long l) {
    return null;
  }

  @Override
  public Bundle[] getBundles() {
    return new Bundle[0];
  }

  @Override
  public void addServiceListener(ServiceListener serviceListener, String s) throws InvalidSyntaxException {
  }

  @Override
  public void addServiceListener(ServiceListener serviceListener) {
  }

  @Override
  public void removeServiceListener(ServiceListener serviceListener) {
  }

  @Override
  public void addBundleListener(BundleListener bundleListener) {
  }

  @Override
  public void removeBundleListener(BundleListener bundleListener) {
  }

  @Override
  public void addFrameworkListener(FrameworkListener frameworkListener) {
  }

  @Override
  public void removeFrameworkListener(FrameworkListener frameworkListener) {
  }

  @Override
  public ServiceRegistration registerService(String[] strings, Object o, Dictionary dictionary) {
    return null;
  }

  @Override
  public ServiceRegistration registerService(String s, Object o, Dictionary dictionary) {
    return null;
  }

  @Override
  public ServiceReference[] getServiceReferences(String s, String s1) throws InvalidSyntaxException {
    return new ServiceReference[0];
  }

  @Override
  public ServiceReference[] getAllServiceReferences(String s, String s1) throws InvalidSyntaxException {
    return new ServiceReference[0];
  }

  @Override
  public ServiceReference getServiceReference(String s) {
    return null;
  }

  @Override
  public Object getService(ServiceReference serviceReference) {
    return null;
  }

  @Override
  public boolean ungetService(ServiceReference serviceReference) {
    return false;
  }

  @Override
  public File getDataFile(String s) {
    return null;
  }

  @Override
  public Filter createFilter(String s) throws InvalidSyntaxException {
    return null;
  }
}

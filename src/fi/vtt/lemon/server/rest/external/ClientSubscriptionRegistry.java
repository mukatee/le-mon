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
package fi.vtt.lemon.server.rest.external;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.vtt.lemon.server.rest.external.resources.Session;

public class ClientSubscriptionRegistry {
  private Map<Long, List<Session>> subscriptions;

  public ClientSubscriptionRegistry() {
    subscriptions = new HashMap<>();
  }

  public void add(long subscribeId, Session client) {
    if (subscriptions.containsKey(subscribeId)) {
      List<Session> sessions = subscriptions.get(subscribeId);
      if (!sessions.contains(client)) {
        sessions.add(client);
        subscriptions.put(subscribeId, sessions);
      }
    } else {
      List<Session> clients = new ArrayList<>();
      clients.add(client);
      subscriptions.put(subscribeId, clients);
    }
  }

  public void remove(long subscribeId, Session session) {
    if (subscriptions.containsKey(subscribeId)) {
      List<Session> sessions = subscriptions.get(subscribeId);
      if (sessions == null) {
        subscriptions.remove(subscribeId);
      } else {
        sessions.remove(session);
        if (sessions.size() == 0) {
          subscriptions.remove(subscribeId);
        }
      }
    }
  }

  public List<Session> get(long subscribeId) {
    return subscriptions.get(subscribeId);
  }
}

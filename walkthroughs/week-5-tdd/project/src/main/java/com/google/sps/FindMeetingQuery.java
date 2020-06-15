// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    int duration = (int) request.getDuration();
    Set<String> attendees = new HashSet<String>(request.getAttendees());
    List<TimeRange> initalRanges = new ArrayList<TimeRange>();
    List<TimeRange> result = new ArrayList<TimeRange>();
    List<Event> eventsArr = events.toArray(new Event[events.size()]);
    
    for(int start = 0; start <= TimeRange.END_OF_DAY; start += duration) {
        if(start + duration > TimeRange.END_OF_DAY) {
            initalRanges.add(TimeRange.fromStartEnd(start, TimeRange.END_OF_DAY, false));
        } else {
            initalRanges.add(TimeRange.fromStartDuration(start, duration));
        }
    }

    for(int i = 0; i < eventsArr.size(); i++) {
        Event currEvent = eventsArr[i];
        Set<String> currAttendees = new HashSet<String>(currEvent.getAttendees());
        for(String attendee : currAttendees) {
            if(attendees.contains(attendee)) {
                blockTimeRange(initalRanges, currEvent);
                break;
            }
        }
    }

    return result;
  }

  private void blockTimeRange(List<TimeRange> &initalRanges, Event event) {

  }
}

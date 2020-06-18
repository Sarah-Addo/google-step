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
    Set<String> attendees = new HashSet<String>(request.getAttendees());
    List<TimeRange> initalRanges = new ArrayList<TimeRange>();
    List<TimeRange> results = new ArrayList<TimeRange>();
    final int minMeetingTime = 15;
    
    //Creates a set of candidate blocks of time, excludes blocks not viable for new meeting,
    //then consolidates remaining blocks into ranges 
    for(int start = 0; start <= TimeRange.END_OF_DAY; start += minMeetingTime) {
      if(start + minMeetingTime > TimeRange.END_OF_DAY) {
          initalRanges.add(TimeRange.fromStartEnd(start, TimeRange.END_OF_DAY, /*inclusivie*/ false));
      } else {
          initalRanges.add(TimeRange.fromStartDuration(start, minMeetingTime));
      }
    }

    for(Event currEvent : events) {
      Set<String> currAttendees = currEvent.getAttendees();
      for(String attendee : currAttendees) {
        if(attendees.contains(attendee)) {
            blockTimeRange(initalRanges, currEvent);
            break;
        }
      }
    }

    TimeRange tempRange = new TimeRange();
    int duration = (int) request.getDuration();

// Group together the leftover time ranges for results
// A range with a non-positive duration signifies that it is a range that will not work for the request
    for(TimeRange range : initalRanges) {

      //if range isn't valid then add tempRange to results then reset it
      if(!range.hasPositiveDuration()) {
        maybeAddToResults(tempRange, results, duration);
        tempRange.setStart(0);
        tempRange.setDuration(0);
        continue;
      }

      //if range is valid then start a new tempRange if there was not one already
      if(!tempRange.hasPositiveDuration()) {
        tempRange.setStart(range.getStart());
        tempRange.setDuration(range.getDuration());
      } else {
        tempRange.setDuration(tempRange.getDuration() + range.getDuration());
      }
    }

    //Add the last range to results if it is viable
    maybeAddToResults(tempRange, results, duration);

    return results;
  }

  public void blockTimeRange(List<TimeRange> ranges, Event event) {
    for(TimeRange range : ranges) {
      if(event.getWhen().overlaps(range)) {
          range.setDuration(0);
      }
    }
  }

  public void maybeAddToResults(TimeRange tempRange, List<TimeRange> results, int validDuration) {
    if(tempRange.getEnd() == TimeRange.END_OF_DAY && tempRange.getDuration() >= validDuration) {
      results.add(TimeRange.fromStartEnd(tempRange.getStart(), TimeRange.END_OF_DAY, /*inclusive*/ true));
    } else if(tempRange.getDuration() >= validDuration) {
        results.add(TimeRange.fromStartDuration(tempRange.getStart(), tempRange.getDuration()));
    }
  }
}

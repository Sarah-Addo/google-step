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
    List<TimeRange> results = new ArrayList<TimeRange>();
    
    for(int start = 0; start <= TimeRange.END_OF_DAY; start += 15) {
        if(start + 15 > TimeRange.END_OF_DAY) {
            initalRanges.add(TimeRange.fromStartEnd(start, TimeRange.END_OF_DAY, false));
        } else {
            initalRanges.add(TimeRange.fromStartDuration(start, 15));
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

    int newRangeStart = -1;
    int newRangeDuration = 0;

// Group together the leftover time ranges for results
    for(TimeRange range : initalRanges) {

        //range is invalid and there was a previous valid range started
        if(!range.isValid() && newRangeStart != -1) {
            if(newRangeDuration >= duration) {
            results.add(TimeRange.fromStartDuration(newRangeStart, newRangeDuration));
            }
            newRangeStart = -1;
            newRangeDuration = 0;
        }

        //range is valid and there is a valid range in the works
        if(range.isValid() && newRangeStart != -1) {
            newRangeDuration += range.duration();
        }

        //range is valid and there is not a vaild range already in the works then start a valid range
        if(range.isValid() && newRangeStart == -1) {
            newRangeStart = range.start();
            newRangeDuration += range.duration();
        }

        //If it is the last range of the initial ranges and there is a viable range in the works and
        //the range duration statisfies the required duration then add it to results
        if(range.end() == TimeRange.END_OF_DAY && newRangeStart != -1 && newRangeDuration >= duration) {
            results.add(TimeRange.fromStartEnd(newRangeStart, TimeRange.END_OF_DAY, true));
        }
    }

    return results;
  }

  public void blockTimeRange(List<TimeRange> ranges, Event event) {
      for(TimeRange range : ranges) {
          if(event.getWhen().overlaps(range)) {
              range.setValid(false);
          }
      }
    }
}

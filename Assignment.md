## Task
The task is to finish implementing two server endpoints for getting 'tips'.

The tips are sourced from two different legacy systems, which are unfortunately rather inconsistent in their naming and representation.
The requirement is to serve them up efficiently, in a single format, as JSON. The exact JSON schema is up to you.

The legacy tip data can be found as CSV files in the resources folder.
We've made the data available via the `LegacyTipsService`, however, so
you will not need to write code the load these files: you can imagine the data is coming directly from legacy service calls.
These services return raw CSV payloads of their tips, as Strings.

To keep things simple, you can assume that the legacy data will not change
during the course of your application's lifetime. No need to worry about
fetching updated data or change-tracking.

There are a finite number of tip types:
- disturbance
- suspicious activity
- bad smell
- arson
- violence

A tip consists of 4 fields:
- a universally unique identifier
- a description
- submission date
- a type

The two endpoints to implement are 
- get tips sorted by submission date (newest first) (`GET /tips`)
- get tip by id (`GET /tips/:id`)
 

# RAlfaBet Backend Exercise - Event-Manager

##     [Swagger documentation!!](https://amirge118.github.io/AlfaBetTest/)

 ### Details

 #### Event
* RESTful API that manages eventsn.
* offering users the ability to schedule, retrieve, update, delete.
* Allow users to:
* Retrieve events based on location.
* Sort events by date, popularity (number of participants), or creation time.
* Allow batch operations for creating, updating, or deleting multiple events in one request.

 #### Reminder
* Send reminders 30 minutes before the event's scheduled time.

#### Rate limiting
* Have rate limiting to prevent API abuse.
#### subscribe
* Allow users to subscribe to events. Notify subscribers via a simulated mechanism (e.g.,
console log) when an event is updated or canceled.


Database architecture:
used PostgreSQL
modules: 
 Event
 Location
 Subscriber
 Subscription

#### many-to-one relationship between two entities:(Event-Location , Subscription-(Subscriber, Event)
* Clarity - It helps express the relationship between entities in a clear and straightforward manner 
* Represents the relationships between objects, making the design more object-oriented.
* The database can optimize queries involving relationships

 Test - 
  EventBatchEventsTest


package meeter.app.test;

class EventNotFoundException extends RuntimeException {

    EventNotFoundException(Long id) {
    super("Could not find event " + id);
  }
}
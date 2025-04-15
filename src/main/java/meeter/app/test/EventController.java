package meeter.app.test;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class EventController {

    private final EventDatabase database;
    private final EventModelAssembler assembler;

    EventController(EventDatabase database, EventModelAssembler assembler) {
        this.database = database;
        this.assembler = assembler;
    }


    // Handler #1
    @GetMapping("/events")
    CollectionModel<EntityModel<Event>> all() {

    List<EntityModel<Event>> events = database.listEvents().stream()
      .map(assembler::toModel)
      .collect(Collectors.toList());

    return CollectionModel.of(events, linkTo(methodOn(EventController.class).all()).withSelfRel());
    }
    
    // Handler #2
    @PostMapping("/events")
    ResponseEntity<?> newEvent(@RequestBody Event newEvent) {

        EntityModel<Event> entityModel = assembler.toModel(database.addEvent(newEvent));

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    // Handler #3
    @GetMapping("/events/{id}")
    EntityModel<Event> one(@PathVariable Long id) {

        Event event = database.listSingle(id)
            .orElseThrow(() -> new EventNotFoundException(id));

        return assembler.toModel(event);
    }

    // Handler #4
    @PutMapping("/events/{id}")
    ResponseEntity<?> replaceEvent(@RequestBody Event newEvent, @PathVariable Long id) {

        Event updatedEvent = database.listSingle(id) //
        .map(event -> {
            event.setName(newEvent.getName());
            event.setCategory(newEvent.getCategory());
            return database.updateEvent(event);
        })
        .orElseGet(() -> {
            return database.updateEvent(newEvent);
        });

        EntityModel<Event> entityModel = assembler.toModel(updatedEvent);

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
            .body(entityModel);
    }

    // Handler #5
    @DeleteMapping("/events/{id}")
    ResponseEntity<?> deleteEvent(@PathVariable Long id) {

        database.deleteEvent(id);

        return ResponseEntity.noContent().build();
    }
}
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

    private final EventRepository repository;

    private final EventModelAssembler assembler;

    EventController(EventRepository repository, EventModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }


    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/events")
    CollectionModel<EntityModel<Event>> all() {

    List<EntityModel<Event>> events = repository.findAll().stream()
      .map(assembler::toModel)
      .collect(Collectors.toList());

    return CollectionModel.of(events, linkTo(methodOn(EventController.class).all()).withSelfRel());
    }
    // end::get-aggregate-root[]

    @PostMapping("/events")
    ResponseEntity<?> newEvent(@RequestBody Event newEvent) {

        EntityModel<Event> entityModel = assembler.toModel(repository.save(newEvent));

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    // Single item

    @GetMapping("/events/{id}")
    EntityModel<Event> one(@PathVariable Long id) {

        Event event = repository.findById(id)
            .orElseThrow(() -> new EventNotFoundException(id));

        return assembler.toModel(event);
    }

    @PutMapping("/events/{id}")
    ResponseEntity<?> replaceEvent(@RequestBody Event newEvent, @PathVariable Long id) {

        Event updatedEvent = repository.findById(id) //
        .map(event -> {
            event.setName(newEvent.getName());
            event.setCategory(newEvent.getCategory());
            return repository.save(event);
        })
        .orElseGet(() -> {
            return repository.save(newEvent);
        });

        EntityModel<Event> entityModel = assembler.toModel(updatedEvent);

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
            .body(entityModel);
    }

    @DeleteMapping("/events/{id}")
    ResponseEntity<?> deleteEvent(@PathVariable Long id) {

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
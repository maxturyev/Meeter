package meeter.app.test;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class EventModelAssembler implements RepresentationModelAssembler<Event, EntityModel<Event>> {

    @Override
    public EntityModel<Event> toModel(Event event) {
  
      return EntityModel.of(event,
          linkTo(methodOn(EventController.class).one(event.getId())).withSelfRel(),
          linkTo(methodOn(EventController.class).all()).withRel("events"));
    }
}

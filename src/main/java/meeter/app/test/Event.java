package meeter.app.test;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
class Event {

  private @Id
  @GeneratedValue Long id;
  private String name;
  private String category;

  Event() {}

  Event(String name, String category) {

    this.name = name;
    this.category = category;
  }

  public Long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getCategory() {
    return this.category;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o)
      return true;
    if (!(o instanceof Event))
      return false;
    Event event = (Event) o;
    return Objects.equals(this.id, event.id) && Objects.equals(this.name, event.name)
        && Objects.equals(this.category, event.category);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.name, this.category);
  }

  @Override
  public String toString() {
    return "Event{" + "id=" + this.id + ", name='" + this.name + '\'' + ", category='" + this.category + '\'' + '}';
  }
}
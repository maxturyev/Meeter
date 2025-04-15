package meeter.app.test;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Event{

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false)
	@NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
	private String name;

	@Column(nullable = false)
	@NotBlank(message = "Category is required")
	private String category;

	public Event() {}

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

}
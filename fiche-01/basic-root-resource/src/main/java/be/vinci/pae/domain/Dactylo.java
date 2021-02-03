package be.vinci.pae.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dactylo {
	
	public final static List<String> levels = Arrays.asList(new String[] {"easy", "medium", "hard"});

	private int id;
	private String content;
	private String level;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLevel() {
		return level;
	}
	
	public void setLevel(String level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "DactyloImpl [id=" + id + ", content=" + content + ", level=" + level + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dactylo other = (Dactylo) obj;
		if (id != other.id)
			return false;
		return true;
	}

}

package net.stephankopp;

import java.time.LocalDate;

public class DocumentData {

	private String universalId;
	private String status;
	private String topic;
	private String description;
	private LocalDate dueDate;
	private boolean changed;
	
	public String getUniversalId() {
		return universalId;
	}
	public void setUniversalId(String universalId) {
		this.universalId = universalId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	public boolean isChanged() {
		return changed;
	}
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
	
	
}

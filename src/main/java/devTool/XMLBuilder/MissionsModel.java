package devTool.XMLBuilder;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "missions")
public class MissionsModel {
	@XmlElement(name = "wtf do i put here lol")
	private LinkedList<MissionModel> missions;

	public MissionsModel() {
		missions = new LinkedList<MissionModel>();
	}

	// helper
	public void addMission(MissionModel m) {
		missions.add(m);
	}

	public void removeMission(MissionModel m) {
		missions.remove(m);
	}

	public String getNextMissionId() {
		return String.valueOf(missions.size());
	}

	// getter
	public LinkedList<MissionModel> getMissions() {
		
		return this.missions;

	}
}
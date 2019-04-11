package devTool.XMLBuilder;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import devTool.models.EditableJob;

public class XMLBuilder {
	private static XMLBuilder instance;
	private String baseDir;
	
	private XMLBuilder() {
	}
	
	public static XMLBuilder getInstance() {
		if (instance == null)
			instance = new XMLBuilder();
		return instance;
	}
	
	public void setBaseDir(File f) {
		this.baseDir = f.getAbsolutePath() + "/";
	}

	public void buildGameModelData(GameModel gm) {
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(GameModel.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(gm, System.out);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public void buildJobFile(EditableJob job) {
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(EditableJob.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			String fileName = "j_" + job.id + ".xml";
			marshaller.marshal(job, new File(this.baseDir + "/jobs/" + fileName));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public void buildMissionsFile(MissionsModel mm) {
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(MissionsModel.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(mm, new File(this.baseDir + "missions.xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}

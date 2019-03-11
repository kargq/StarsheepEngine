package engine.starsheep.space;

import java.io.File;
import java.util.List;

import engine.starsheep.space.Job.Job;
import engine.starsheep.space.Job.JobFlyer;

/**
 * Use this file to run your tests.
 */
public class StarTest {

    public void testFileReading() {
        Job j = StarReader.readJob(new File("/Users/kindeep/Projects/StarsheepEngine/StarsheepEngine/res/game_files/Job.xml"), 999);
        System.out.println(j.toString());
    }

    public void testReadingMissions() {
        MissionsManager missionsManager = MissionsManager.getInstance();
        List<Mission> missions = missionsManager.getMissions();
        missions.forEach(m -> {
            List<JobFlyer> flyers = m.getJobFlyers();
            flyers.forEach(flyer -> {
                System.out.println(flyer.getName());
                System.out.println(flyer.getDescription());
                System.out.println(flyer.getJobId());
                System.out.println(flyer.getStaminaCost());
            });
        });
    }
}

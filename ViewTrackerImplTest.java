import java.util.Collection;
public class ViewTrackerImplTest {
  public static void main(String[] args) {
    ViewTrackerImpl vt = new ViewTrackerImpl();
    Collection<String> admins = vt.getAdmins("abc");
    System.out.println(admins);
    vt.trackView("admin1", "page1");
    Collection<String> realAdmins = vt.getAdmins("page1");
    System.out.println("page1: " + realAdmins);
    Collection<String> realPages = vt.getPages("admin1");
    System.out.println("admi1: " + realPages);
    vt.trackView("admin1", "page2");
    vt.trackView("admin2", "page2");
    (new Thread( () -> {
      try{
        Thread.sleep(25000);
      } catch (Exception e){
        System.err.println(e);
      }

      Collection<String> againAdmins = vt.getAdmins("page1");
      System.out.println(againAdmins);
      Collection<String> againPages = vt.getPages("admin1");
      System.out.println(againPages);
    } )).start();
  }
}

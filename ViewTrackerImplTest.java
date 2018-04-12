import java.util.Collection;
public class ViewTrackerImplTest {
  public static void main(String[] args) {
    ViewTrackerImpl vt = new ViewTrackerImpl();
    Collection<String> admins = vt.getAdmins("abc");
    System.out.println("No admins visiting page abc: " + admins);
    System.out.println("admin1 visiting page1");
    vt.trackView("admin1", "page1");
    vt.trackView("admin1", "page1");
    Collection<String> realAdmins = vt.getAdmins("page1");
    System.out.println("page1: " + realAdmins);
    Collection<String> realPages = vt.getPages("admin1");
    System.out.println("admin1: " + realPages);
    System.out.println("admin1 visiting page2");
    vt.trackView("admin1", "page2");
    System.out.println("admin2 visiting page2");
    vt.trackView("admin2", "page2");
    System.out.println("admin2 visiting page3");
    vt.trackView("admin2", "page3");
    Collection<String> page2AdminCol = vt.getAdmins("page2");
    System.out.println("page2: " + page2AdminCol);
    Collection<String> admin2Pages = vt.getPages("admin2");
    System.out.println("admin2: " + admin2Pages);
    (new Thread( () -> {
      try{
        Thread.sleep(25000);
      } catch (Exception e){
        System.err.println(e);
      }

      Collection<String> againAdmins = vt.getAdmins("page1");
      System.out.println("page1: " + againAdmins);
      Collection<String> againPages = vt.getPages("admin1");
      System.out.println("admin1: " + againPages);
      try{
        Thread.sleep(5000);
      } catch (Exception e){
        System.err.println(e);
      }
      Collection<String> page2Admins = vt.getAdmins("page2");
      System.out.println("page2: " + page2Admins);
    } )).start();
  }
}

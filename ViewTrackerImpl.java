import java.util.HashMap;
import java.util.Collection;
import java.util.Vector;
import java.util.Timer;
import java.util.TimerTask;
import java.time.Instant;

public class ViewTrackerImpl implements ViewTracker {

  private HashMap<String, HashMap> pageAdmins;
  private HashMap<String, HashMap> adminPages;
  private static final int DELAY = 10;

  private class CheckViews extends TimerTask {
    public void run(){
      expire();
    }
  }

  public ViewTrackerImpl () {
    this.pageAdmins = new HashMap<String, HashMap>();
    this.adminPages = new HashMap<String, HashMap>();
    Timer timer = new Timer();
    CheckViews check = new CheckViews();
    timer.scheduleAtFixedRate(check, 1000*DELAY, 1000*DELAY);
  }

  private void addRelation(String k, String v, HashMap h) {
    if(h.containsKey(k)) {
      HashMap values = (HashMap)h.get(k);
      values.put(v, Instant.now());
    } else {
      HashMap values = new HashMap<String, Instant>();
      values.put(v, Instant.now());
      h.put(k, values);
    }
  }

/**
  private void addPage (String admin, String pageId) {
    if (adminPages.containsKey(admin)) {
      HashMap pages = adminPages.get(admin);
      pages.put(pageId, true);
    } else {
      HashMap pages = new HashMap<String, Boolean>();
      pages.put(pageId, true);
      adminPages.put(admin, pages);
    }
  }

  private void addAdmin (String admin, String pageId) {
    if(pageAdmins.containsKey(pageId)) {
      HashMap admins = pageAdmins.get(pageId);
      admins.put(admin, true);
    } else {
      HashMap admins = new HashMap<String, Boolean>();
      admins.put(pageId, true);
      pageAdmins.put(pageId, admins);
    }
  }
*/

  public void trackView(String admin, String pageId) {
    this.addRelation(admin, pageId, adminPages);
    this.addRelation(pageId, admin, pageAdmins);
  }

  public Collection<String> getAdmins(String pageId){
    HashMap adminsHash = (HashMap)pageAdmins.get(pageId);
    if(adminsHash == null){
      return null;
    }
    return adminsHash.keySet();
  }

  public  Collection<String> getPages(String admin){
    HashMap pagesHash = (HashMap)adminPages.get(admin);
    if(pagesHash == null){
       return null;
     }
    return pagesHash.keySet();
  }

  private void removeViewTrack(String admin, String pageId) {
    System.out.println("Removing (admin, pageId): " + "(" + admin +"," + pageId + ")");
    HashMap views = (HashMap)adminPages.get(admin);
    views.remove(pageId);
    HashMap admins = (HashMap)pageAdmins.get(pageId);
    admins.remove(admin);
  }

  private void expire() {
    HashMap listToRemove = new HashMap<String, String>();
    System.out.println("Looking for expired sessions...");
    for (String page : (Collection<String>)pageAdmins.keySet()) {
      HashMap admins = (HashMap)pageAdmins.get(page);
      for (String admin : (Collection<String>)admins.keySet()) {
        Instant creation = (Instant)admins.get(admin);
        if(creation.isBefore(Instant.now().minusSeconds(DELAY))){
          listToRemove.put(admin, page);
        }
      }
    }
    for(String admin : (Collection<String>)listToRemove.keySet()){
      removeViewTrack(admin, (String)listToRemove.get(admin));
    }
  }
}

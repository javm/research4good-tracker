import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;
import java.util.Vector;
import java.util.Timer;
import java.util.TimerTask;
import java.time.Instant;

public class ViewTrackerImpl implements ViewTracker {

  private ConcurrentHashMap<String, ConcurrentHashMap> pageAdmins;
  private ConcurrentHashMap<String, ConcurrentHashMap> adminPages;
  private static final int DELAY = 10;

  private class CheckViews extends TimerTask {
    public void run(){
      expire();
    }
  }

  public ViewTrackerImpl () {
    this.pageAdmins = new ConcurrentHashMap<String, ConcurrentHashMap>();
    this.adminPages = new ConcurrentHashMap<String, ConcurrentHashMap>();
    Timer timer = new Timer();
    CheckViews check = new CheckViews();
    timer.scheduleAtFixedRate(check, 1000*DELAY, 1000*DELAY);
  }

  private void addRelation(String k, String v, ConcurrentHashMap h) {
    if(h.containsKey(k)) {
      ConcurrentHashMap values = (ConcurrentHashMap)h.get(k);
      values.put(v, Instant.now());
    } else {
      ConcurrentHashMap values = new ConcurrentHashMap<String, Instant>();
      values.put(v, Instant.now());
      h.put(k, values);
    }
  }

  public void trackView(String admin, String pageId) {
    this.addRelation(admin, pageId, adminPages);
    this.addRelation(pageId, admin, pageAdmins);
  }

  public Collection<String> getAdmins(String pageId){
    ConcurrentHashMap adminsHash = (ConcurrentHashMap)pageAdmins.get(pageId);
    if(adminsHash == null){
      return null;
    }
    return adminsHash.keySet();
  }

  public  Collection<String> getPages(String admin){
    ConcurrentHashMap pagesHash = (ConcurrentHashMap)adminPages.get(admin);
    if(pagesHash == null){
       return null;
     }
    return pagesHash.keySet();
  }

  private void removeViewTrack(String admin, String pageId) {
    System.out.println("Removing (admin, pageId): " + "(" + admin +"," + pageId + ")");
    ConcurrentHashMap views = (ConcurrentHashMap)adminPages.get(admin);
    views.remove(pageId);
    ConcurrentHashMap admins = (ConcurrentHashMap)pageAdmins.get(pageId);
    admins.remove(admin);
  }

  private void expire() {
    ConcurrentHashMap listToRemove = new ConcurrentHashMap<String, String>();
    System.out.println("Looking for expired sessions...");
    for (String page : (Collection<String>)pageAdmins.keySet()) {
      ConcurrentHashMap admins = (ConcurrentHashMap)pageAdmins.get(page);
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

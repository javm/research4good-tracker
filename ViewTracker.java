import java.util.Collection;

/**
 * Code Interview Challenge
 *
 * Create a Data Structure for simultaneously tracking what pages are currently being 
 * viewed by each admin, and which admins are viewing each page.
 *
 * Pages will notify the ViewTracker every 5 seconds that they are still being viewed. 
 * If more than 10 seconds passes without a call to trackView, a page should be considered closed.
 *
 * The implementation should be
 * 1. Thread-safe (different threads will call trackView and many threads will simultaneously call getPages or getAdmins)
 * 2. As fast as possible. These methods will be called very frequently
 * 3. Not leak memory. Once a page view is more than 10 seconds old it may be discarded.
 */
public interface ViewTracker {

	/**
	* This method is called to indicate that a page is currently being viewed by an admin. The web page typically has
	* a JavaScript timer which notifies the server to call this method every 5 seconds. If more than 10 seconds pass without 
	* receiving the method call, the ViewTracker may assume that the browser is no longer displaying the page.
	* @param admin the username of the admin viewing the page
	* @param pageId the id of the page
	*/
	public void trackView(String admin, String pageId);

	/**
	*
	* @param pageId the id of the page
	* @return a collection of the admins currently viewing this page
	*/
	public Collection getAdmins(String pageId);

	/**
	*
	* @param admin the username of the admin
	* @return a collection of the pages currently being viewed by this admin
	*/
	public Collection getPages(String admin);

}


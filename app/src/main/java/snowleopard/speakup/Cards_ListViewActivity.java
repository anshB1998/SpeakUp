package snowleopard.speakup;
import android.widget.ImageButton;


/**
 * Created by aman on 7/10/17.
 */

class Cards_ListViewActivity {
    private String Title, Description, ImageUrl,Owner;

    public Cards_ListViewActivity() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageURL() {
        return ImageUrl;
    }

    public void setImageURL(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public Cards_ListViewActivity(String title, String description, String imageURL, String owner) {
        Title = title;
        Description = description;
        ImageUrl = imageURL;
        Owner = owner;
    }
}

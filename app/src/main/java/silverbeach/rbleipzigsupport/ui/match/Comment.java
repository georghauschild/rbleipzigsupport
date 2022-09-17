package silverbeach.rbleipzigsupport.ui.match;

public class Comment {

    private String commentText, user, uid;

    public Comment() {
    }

    public Comment(String commentText, String user, String uid) {
        this.commentText = commentText;
        this.user = user;
        this.uid = uid;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

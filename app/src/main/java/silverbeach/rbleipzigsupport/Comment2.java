package silverbeach.rbleipzigsupport;

class Comment2 {

    private String commentText, user, uid;

    public Comment2() {
    }

    public Comment2(String commentText, String user, String uid) {
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

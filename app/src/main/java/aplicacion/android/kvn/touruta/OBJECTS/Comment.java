package aplicacion.android.kvn.touruta.OBJECTS;

public class Comment {
    private int commentId;
    private int commentTourId;
    private int commentUserId;
    private String commentContent;

    public Comment(int commentTourId, int commentUserId, String commentContent) {
        this.commentTourId = commentTourId;
        this.commentUserId = commentUserId;
        this.commentContent = commentContent;
    }

    public Comment() {
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public int getCommentId() {
        return commentId;
    }

    public int getCommentTourId() {
        return commentTourId;
    }

    public int getCommentUserId() {
        return commentUserId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentTourId(int commentTourId) {
        this.commentTourId = commentTourId;
    }

    public void setCommentUserId(int commentUserId) {
        this.commentUserId = commentUserId;
    }
}

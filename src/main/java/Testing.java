public class Testing {


    public static void main (String[] args) {
        ShounenClient client = new ShounenClient("mfeWxfHNFQ");
        System.out.println(client.postUser(new User("116275390695079945", "Nadeko")));
        System.out.println(client.deleteUser("116275390695079945"));
        // System.out.println(client.addAnime("166883258200621056", "One Piece"));
        // System.out.println(client.getUser("116275390695079945"));

    }

}

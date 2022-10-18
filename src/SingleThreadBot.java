public class SingleThreadBot extends Thread {
	int Identity; // This integer variable will be the thread identifier
	String init_char;// This character will be used by each thread as the first letter in the
						// password

	// Here we redefine the default constructor of this class.
	// By default it has no arguments, but in this example
	// We are using two arguments
	public SingleThreadBot(int id) {
		// Here we retrieve the value of the identity passed by the main class
		Identity = id;
		// Here we retrieve the value of the character passed by the main class
		// init_char = c;
	}

	public void run() {

		String password = "";
		String[] characterList = { "i", "v", "t" };

		for (int character = 0; character < characterList.length; character++) {
			// Thread trigging (starting the thread
			init_char = characterList[character];

			// Here is where you write the code that should crack the password

			// Each for loop goes through each character in the 4 letter password

			for (int i = 97; i <= 122; i++) {
				for (int j = 97; j <= 122; j++) {
					for (int k = 97; k <= 122; k++) {
						for (int l = 97; l <= 122 && !ThreadAttacker.found; l++) {

							char first = (char) i;
							char second = (char) j;
							char third = (char) k;
							char fourth = (char) l;

							password = "" + init_char + first + second + third + fourth;

							String tempx = password + ThreadAttacker.challenge;
							int hash = tempx.hashCode();

							//System.out.println(password);

							if (hash == ThreadAttacker.captured) {
								// Exit this thread and notify all other thread that the password has been found
								ThreadAttacker.found = true;
								System.out.println(Identity);
							}

						}
					}
				}
			}
		}

	}
}

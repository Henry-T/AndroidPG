
public class EnumTest {
	public static void main(String[] args) {
		System.out.println(ETest.One.name());
	}

	enum ETest {
		One,
		Two;
	}
}

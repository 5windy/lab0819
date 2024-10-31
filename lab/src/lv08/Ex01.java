package lv08;

import java.util.ArrayList;
import java.util.Scanner;

class Tv {
	private String name;
	private String brand;
	private int price;

	public Tv(String name, String brand, int price) {
		this.name = name;
		this.brand = brand;
		this.price = price;
	}
	
	public Tv(String name, String brand) {
		this.name = name;
		this.brand = brand;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getBrand() {
		return this.brand;
	}
	
	public int getPrice() {
		return this.price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Tv) {
			Tv target = (Tv)obj;
			return target.getName().equals(this.name) && target.getBrand().equals(this.brand);
		}
		return false;
	}
	
	@Override
	public String toString() {
		String info = String.format("%s (%s) : %d원", name, brand, price);
		return info;
	}
}

class ERP {
	private final int ADD = 1;
	private final int INSERT = 2;
	private final int REMOVE = 3;
	private final int REMOVE_VALUE = 4;
	private final int READ = 5;
	private final int UPDATE = 6;
	private final int SIZE = 7;
	
	private final int STRING = 1;
	private final int NUMBER = 2;
	
	private final int READ_NAME = 1;
	private final int READ_BRAND = 2;
	private final int READ_PRICE = 3;
	
	private final int UPDATE_NAME = 1;
	private final int UPDATE_PRICE = 2;
	
	private Scanner scanner = new Scanner(System.in);
	
	private String title;
	
	private ArrayList<Tv> tvList = new ArrayList<>();
	
	
	public ERP(String title) {
		this.title = title;
	}
	
	public void run() {
		while(true) {
			showDataAll();
			showMenu();
			runMenu();
		}
	}
	
	private void showDataAll() {
		for(int i=0; i<tvList.size(); i++)
			System.out.println(tvList.get(i));
	}
	
	private void showDataAll(ArrayList<Tv> list) {
		if(list.size() == 0)
			System.err.println("조회되는 상품이 없습니다.");
		
		for(int i=0; i<list.size(); i++) {
			Tv tv = list.get(i);
			System.out.println(tv);
		}
	}
	
	private void showMenu() {
		System.out.println("1) 추가");
		System.out.println("2) 삽입");
		System.out.println("3) 삭제(인덱스)");
		System.out.println("4) 삭제(값)");
		System.out.println("5) 조회");
		System.out.println("6) 수정");
		System.out.println("7) 크기");
	}
	
	private void runMenu() {
		int select = (int)input("메뉴 선택", NUMBER);
		
		if(select == ADD)
			addTv();
		else if(select == INSERT)
			insertTv();
		else if(select == REMOVE)
			removeTvByIndex();
		else if(select == REMOVE_VALUE)
			removeTvByValue();
		else if(select == READ)
			showTv();
		else if(select == UPDATE)
			updateTv();
		else if(select == SIZE)
			System.out.println(size());
	}
	
	private void addTv() {
		Tv tv = createTv();
		
		// 중복 예외처리
		if(tvList.contains(tv)) {
			System.err.println("동일한 상품은 중복등록이 불가합니다.");
			return;
		}
		tvList.add(tv);
	}
	
	private Tv createTv() {
		String name = (String)input("TV 이름", STRING);
		String brand = (String)input("TV 브랜드", STRING);
		int price = (int)input("TV 가격", NUMBER);
		
		Tv tv = new Tv(name, brand, price);
		
		return tv;
	}
	
	private void insertTv() {
		Tv tv = createTv();
		
		if(tvList.contains(tv)) {
			System.err.println("동일한 상품은 중복등록이 불가합니다.");
			return;
		}
		
		int index = (int)input("삽입할 인덱스", NUMBER);
		
		tvList.add(index, tv);
	}
	
	private void removeTvByIndex() {
		int index = (int)input("삽입할 인덱스", NUMBER);
		
		tvList.remove(index);
	}
	
	private void removeTvByValue() {
		String name = (String)input("삭제할 TV 이름", STRING);
		String brand = (String)input("삭제할 TV 브랜드", STRING);
		
		Tv tv = new Tv(name, brand);
		
		if(!tvList.contains(tv)) {
			System.out.println("존재하지 않는 상품 정보입니다.");
			return;
		}
		
		tvList.remove(tv);
	}
	
//	private Tv findTvByNameAndBrand(String name, String brand) {
//		for(int i=0; i<tvList.size(); i++) {
//			Tv target = tvList.get(i);
//			// 중복 조건 : 이름 + 브랜드 모두 일치하면
//			if(name.equals(target.getName()) &&
//					brand.equals(target.getBrand()))
//				return target;
//		}
//		return null;
//	}
	
	private void showTv() {
		System.out.println("1) 이름으로 조회");
		System.out.println("2) 브랜드명으로 조회");
		System.out.println("3) 가격으로 조회");
		
		int select = (int)input("옵션 선택", NUMBER);
		
		if(select == READ_NAME)
			showTvByName();
		else if(select == READ_BRAND)
			showTvByBrand();
		else if(select == READ_PRICE)
			showTvByPrice();
	}
	
	private void showTvByName() {
		String name = (String)input("조회할 TV 이름", STRING);
		ArrayList<Tv> list = findTvAllByName(name);
		showDataAll(list);
	}
	
	private void showTvByBrand() {
		String brand = (String)input("조회할 TV 브랜드", STRING);
		ArrayList<Tv> list = findTvAllByBrand(brand);
		showDataAll(list);
	}
	
	private void showTvByPrice() {
		int price = (int)input("조회할 TV 가격", NUMBER);
		ArrayList<Tv> list = findTvAllByPrice(price);
		showDataAll(list);
	}
	
	private ArrayList<Tv> findTvAllByName(String name) {
		ArrayList<Tv> list = new ArrayList<>();
		
		for(int i=0; i<tvList.size(); i++) {
			Tv tv = tvList.get(i);
			if(name.equals(tv.getName()))
				list.add(tv);
		}
		
		return list;
	}
	
	private ArrayList<Tv> findTvAllByBrand(String brand) {
		ArrayList<Tv> list = new ArrayList<>();
		
		for(int i=0; i<tvList.size(); i++) {
			Tv tv = tvList.get(i);
			if(brand.equals(tv.getBrand()))
				list.add(tv);
		}
		
		return list;
	}
	
	private ArrayList<Tv> findTvAllByPrice(int price) {
		ArrayList<Tv> list = new ArrayList<>();
		
		for(int i=0; i<tvList.size(); i++) {
			Tv tv = tvList.get(i);
			if(price == tv.getPrice())
				list.add(tv);
		}
		
		return list;
	}
	
	private void updateTv() {
		String name = (String)input("대상 TV 이름", STRING);
		String brand = (String)input("대상 TV 브랜드", STRING);
		
		Tv tv = new Tv(name, brand);
		
		if(!tvList.contains(tv)) {
			System.out.println("조회되지 않는 상품입니다.");
			return;
		}
		
		System.out.println("1) 이름 변경");
		System.out.println("2) 가격 변경");
		
		int select = (int)input("옵션 선택", NUMBER);
		
		if(select == UPDATE_NAME)
			updateName(tv);
		else if(select == UPDATE_PRICE)
			updatePrice(tv);
	}
	
	private void updateName(Tv tv) {
		int index = tvList.indexOf(tv);
		Tv target = tvList.get(index);
		
		String name = (String)input("변경할 이름", STRING);
		target.setName(name);
	}
	
	private void updatePrice(Tv tv) {
		int index = tvList.indexOf(tv);
		Tv target = tvList.get(index);
		
		
		int price = (int)input("변경할 가격", NUMBER);
		target.setPrice(price);
	}
	
	private int size() {
		return this.tvList.size();
	}
	
	private Object input(String message, int type) {
		System.out.print(message + " : ");
		String input = "";
		
		switch(type) {
		case STRING:
			while(input.equals(""))
				input = scanner.nextLine();
			return input;
		case NUMBER:
			int number = 0;
			try {
				input = scanner.nextLine();
				number = Integer.parseInt(input);
				return number;
			} catch (Exception e) {
				System.err.println("숫자를 입력해주세요.");
			}
		default :
			return null;
		}
	}
}

public class Ex01 {
	public static void main(String[] args){
		
		ERP system = new ERP("더조은회사");
		system.run();

	}
}
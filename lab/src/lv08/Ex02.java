package lv08;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class Subject {
	private int code;
	private String title;
	private int score;
	
	public Subject(String title) {
		this.title = title;
	}
	
	public Subject(int code, String title) {
		this.code = code;
		this.title = title;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Subject) {
			Subject target = (Subject)obj;
			return target.getTitle().equals(this.title);
		}
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		String info = String.format("%d) %s", code, title);
		return info;
	}
}

class Student {
	private int code;
	private String name;
	private ArrayList<Subject> subjects = new ArrayList<>();

	public Student(int code) {
		this.code = code;
	}
	
	public Student(int code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Subject getSubjectByCode(int code) {
		Subject subject = null;
		
		for(int i=0; i<subjects.size(); i++) {
			Subject target = subjects.get(i);
			if(target.getCode() == code)
				subject = target;
		}
		return subject;
	}
	
	public Subject getSubjectByIndex(int index) {
		return subjects.get(index);
	}
	
	public void addSubject(Subject subject) {
		// 중복 예외처리 
		if(subjects.contains(subject)) {
			System.out.println("이미 수강중인 과목입니다. ");
			return;
		}
		subjects.add(subject);
	}

	public void removeSubject(Subject subject) {
		// 삭제할 인덱스 
		int index = subjects.indexOf(subject);
		
		if(index == -1) {
			System.out.println("존재하지 않는 과목입니다.");
			return;
		}
		subjects.remove(index);
	}
	
	public String stringSubjectAll() {
		String str = "";
		for(int i=0; i<subjects.size(); i++) {
			Subject subject = subjects.get(i);
			
			str += i + 1 + " : ";
			str += subject;
			str += ", " + subject.getScore();
			
			if(i < subjects.size() - 1)
				str += "\n";
		}
		return str;
	}
	
	public int subjectsSize() {
		return this.subjects.size();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Student) {
			Student target = (Student)obj;
			return target.getCode() == this.code;
		}
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		String info = String.format("학번 : %d\n이름: %s\n", code, name);
		info += stringSubjectAll();
		return info;
	}
}

class Lms {
	private final int JOIN_STUDENT = 1;
	private final int DELETE_STUDENT = 2;
	private final int JOIN_SUBJECT = 3;
	private final int DELETE_SUBJECT = 4;
	private final int ADD_SUBJECT = 5;
	private final int UPDATE_SCORE = 6;
	private final int SHOW_SCORE = 7;
	
	private final int STRING = 1;
	private final int NUMBER = 2;
	
	private Scanner scanner = new Scanner(System.in);
	private Random random = new Random();
	
	private ArrayList<Student> group = new ArrayList<>();
	private ArrayList<Subject> subjects = new ArrayList<>();

	public void run() {
		while(true) {
			System.out.println("--- 더조은 ---");
			showStudentAll();
			System.out.println("------------");
			printMenu();
			runMenu();
		}
	}
	
	private void printMenu() {
		System.out.println("1) 학생등록");
		System.out.println("2) 학생제적");
		System.out.println("3) 수강신청");
		System.out.println("4) 수강철회");
		System.out.println("5) 과목개설");
		System.out.println("6) 성적산정");
		System.out.println("7) 성적조회");
	}
	
	private void runMenu() {
		int select = (int)input("메뉴 선택", NUMBER);
		
		if(select == JOIN_STUDENT)
			joinStudent();
		else if(select == DELETE_STUDENT)
			deleteStudent();
		else if(select == JOIN_SUBJECT)
			joinSubject();
		else if(select == DELETE_SUBJECT)
			deleteSubject();
		else if(select == ADD_SUBJECT)
			addSubject();
		else if(select == UPDATE_SCORE)
			updateScore();
		else if(select == SHOW_SCORE)
			showScore();
	}
	
	private void joinStudent() {
		Student student = createStudent();
		group.add(student);
		System.out.println("학생등록 완료");
	}
	
	private Student createStudent() {
		int code = generateStudentCode();
		String name = (String)input("학생 이름", STRING);
		Student student = new Student(code, name);
		return student;
	}
	
	private int generateStudentCode() {
		int code = 0;
		while(true) {
			code = random.nextInt(9000) + 1000;
			Student student = new Student(code);
			
			if(!group.contains(student))
				break;
		}
		return code;
	}
	
	private void deleteStudent() {
		int code = (int)input("학번 입력", NUMBER);
		Student temp = new Student(code);
		
		int index = group.indexOf(temp);
		group.remove(index);
		System.out.println("제적처리 완료");
	}
	
	private void joinSubject() {
		// 1. 학생 조회
		int code = (int)input("학번 입력", NUMBER);
		Student student = findStudentByCode(code);
		
		if(student == null) {
			System.out.println("조회되지 않는 학생입니다.");
			return;
		}
		
		// 2. 신청 과목 선택
		showSubjectAll();
		int index = (int)input("과목 선택", NUMBER) - 1;
		
		if(index < 0 || index >= subjects.size()) {
			System.out.println("유효하지 않은 번호입니다.");
			return;
		}
		
		Subject target = subjects.get(index);
		Subject subject = new Subject(target.getCode(), target.getTitle());
		
		// 3. 추가 처리
		student.addSubject(subject);
	}
	
	private void showSubjectAll() {
		for(int i=0; i<subjects.size(); i++) {
			System.out.print(i + 1 + " : ");
			System.out.println(subjects.get(i));
		}
	}
	
	private void showStudentAll() {
		for(int i=0; i<group.size(); i++) {
			System.out.println(group.get(i));
		}
	}
	
	private Student findStudentByCode(int code) {
		Student temp = new Student(code);
		int index = group.indexOf(temp);
		
		if(index == -1)
			return null;
		
		// 실 객체 가져오기
		Student student = group.get(index);
		return student;
	}
	
	private Subject createSubject() {
		int code = generateSubjectCode();
		String title = (String)input("과목명", STRING);
		Subject subject = new Subject(code, title);
		return subject;
	}
	
	private int generateSubjectCode() {
		int code = 0;
		while(true) {
			code = random.nextInt(9000) + 1000;
			
			boolean isDupl = false;
			for(int i=0; i<subjects.size(); i++) {
				Subject subject  = subjects.get(i);
				if(subject.getCode() == code) 
					isDupl = true;
			}
			
			if(!isDupl)
				break;
		}
		return code;
	}
	
	private void deleteSubject() {
		// 1. 학생 조회 
		int code = (int)input("학번 입력", NUMBER);
		Student student = findStudentByCode(code);
		
		if(student == null) {
			System.out.println("조회되지 않는 학생입니다.");
			return;
		}
		
		// 2. 학생이 수강신청한 과목의 목록 출력 
		System.out.println(student.stringSubjectAll());
		
		// 3. 철회할 과목 번호 입력 
		int index = (int)input("과목 선택", NUMBER) - 1;
		
		if(index < 0 || index >= student.subjectsSize()) {
			System.out.println("유효하지 않은 번호입니다.");
			return;
		}
		
		// 4. 처리
		Subject subject = student.getSubjectByIndex(index);
		student.removeSubject(subject);
		
		System.out.println("수강철회 완료");
	}
	
	private void addSubject() {
		Subject subject = createSubject();
		if(subjects.contains(subject)) {
			System.out.println("이미 개설된 과목입니다.");
			return;
		}
		subjects.add(subject);
	}
	
	private void updateScore() {
		// 1. 학생 조회 
		int code = (int)input("학번 입력", NUMBER);
		Student student = findStudentByCode(code);
		
		if(student == null) {
			System.out.println("조회되지 않는 학생입니다.");
			return;
		}
		
		// 2. 수강신청한 과목 출력 
		System.out.println(student.stringSubjectAll());
		
		// 3. 과목 선택 
		int index = (int)input("과목 선택", NUMBER) - 1;
		
		if(index < 0 || index >= student.subjectsSize()) {
			System.out.println("유효하지 않은 번호입니다.");
			return;
		}
		
		// 4. 수정
		int score = (int)input("성적 입력", NUMBER);
		
		if(score < 0 || score > 100) {
			System.out.println("유효하지 않은 성적의 범위입니다.");
			return;
		}
		
		Subject subject = student.getSubjectByIndex(index);
		subject.setScore(score);
		
		System.out.println("성적반영 완료");
	}
	
	private void showScore() {
		int code = (int)input("학번 입력", NUMBER);
		Student student = findStudentByCode(code);
		
		if(student == null) {
			System.out.println("조회되지 않는 학생입니다.");
			return;
		}
		
		System.out.println(student);
	}
	
	private Object input(String message, int type) {
		System.out.print(message + " : ");
		String input = "";
		
		switch(type) {
		case STRING : 
			while(input.equals(""))
				input = scanner.nextLine();
			return input;
		case NUMBER :
			int number = 0;
			try {
				input = scanner.nextLine();
				number = Integer.parseInt(input);
				return number;
			} catch (Exception e) {
				System.err.println("숫자를 입력하세요.");
			}
		default : return null;
		}
	}
}

public class Ex02 {
	public static void main(String[] args) {
		Lms system = new Lms();
		system.run();
	}
}
package lv08;

import java.util.ArrayList;

// Generic Class 제네릭 클래스 
// ㄴ 기능이 동일하고, "다루는 객체의 타입"만 다른 경우 
// ㄴ (클래스)객체 생성 단계에서 "다루는 객체의 타입"을 명세하도록 만든 클래스 

class MyVector<E> {
	
	private int size;
	private Object[] list;
	
	// add
	public boolean add(E element) {
		Object[] temp = list;
		list = new Object[size + 1];
		
		for(int i=0; i<size; i++)
			list[i] = temp[i];
		list[size ++] = element;
		
		return true;
	}
	
	// add
	public void add(int index, E element) {
		if(index < 0 || index > size)
			return;
		
		Object[] temp = list;
		list = new Object[size + 1];
		
		for(int i=0; i<size; i++) {
			if(i < index)
				list[i] = temp[i];
			else
				list[i + 1] = temp[i];
		}
		list[index] = element;
		size ++;
	}
	
	// remove
	public E remove(int index) {
		E element = null;
		
		if(index >= 0 && index < size) {
			E target = (E)list[index];
			
			if(remove(target))
				element = target;
		}
		return element;
	}
	
	// remove
	public boolean remove(Object object) {
		int index = -1;
		for(int i=0; i<size; i++) {
			if(object.equals(list[i]))
				index = i;
		}
		
		if(index == -1)
			return false;
		
		Object[] temp = list;
		list = new Object[size - 1];
		
		int n = 0;
		for(int i=0; i<size; i++) {
			if(i != index)
				list[n ++] = temp[i]; 
		}
		size --;
		return true;
	}
	
	// isEmpty
	public boolean isEmpty() {
		return size == 0;
	}
	
	// clear
	public void clear() {
		list = null;
		size = 0;
	}
	
	// clone
	public Object clone() {
		Object obj = null;
		
		MyVector<E> copy = new MyVector<>();
		
		for(int i=0; i<size; i++)
			copy.add((E)list[i]);
		
		obj = copy;
		return obj;
	}
	
	// size
	public int size() {
		return this.size;
	}
	
	// get
	public E get(int index) {
		return (E)list[index];
	}
	
	// set
	public E set(int index, E element) {
		if(index < 0 || index >= size)
			return null;
		
		list[index] = element;
		return element;
	}
	
	// indexOf
	public int indexOf(Object o) {
		int index = -1;
		
		for(int i=0; i<size; i++) {
			if(list[i].equals(o))
				index = i;
		}
		return index;
	}
	
	// contains
	public boolean contains(Object o) {
		for(int i=0; i<size; i++) {
			if(list[i].equals(o))
				return true;
		}
		return false;
	}
}

// 1 to 50
// ㄴ map : 5 * 5
// ㄴ MyVector 활용하여 완성

class Block {
	
}

class OneToFifty {
	
}

public class Ex03 {
	public static void main(String[] args) {
		
		OneToFifty game = new OneToFifty();
		game.run();
	}

}

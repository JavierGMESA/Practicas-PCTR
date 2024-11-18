package P7;
/* Copyright (C) 2006 M. Ben-Ari. See copyright.txt */
class PCMonitor {
	final int N;
	int Oldest = 0, Newest = 0;
  	volatile int Count = 0;
	int[] Buffer;

	public PCMonitor(int tam)
	{
		N = tam;
		Buffer = new int[tam];
	}

	synchronized void Append(int V) {
    while (Count == N)
      try {
         wait();
      } catch (InterruptedException e) {}
		Buffer[Newest] = V;
		Newest = (Newest + 1) % N;
		Count = Count + 1;
		notifyAll();
	}

	synchronized int Take() {
		int temp;
    while (Count == 0)
      try {
         wait();
      } catch (InterruptedException e) {}
		temp = Buffer[Oldest];
		Oldest = (Oldest + 1) % N;
		Count = Count - 1;
		notifyAll();
		return temp;
	}
}

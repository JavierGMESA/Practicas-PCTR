#ifndef SIMBARRIER
#define SIMBARRIER

#include <stdio.h>
#include <iostream>
#include <cassert>
#include <mutex>
#include <condition_variable>
#include <thread>

class simBarrier
{
public:
    simBarrier(int n_hebras): nh_maximo{n_hebras}, nh_actual{n_hebras}{}
    void toWaitOnBarrier();
    void resetBarrier();
private:
    int nh_maximo, nh_actual;
    std::mutex cerrojo;
    std::condition_variable dormir;
};

void ejecucionHebras(simBarrier& bar, std::mutex& cerrojo);

#endif
#include "simBarrier.hpp"

void simBarrier::toWaitOnBarrier()
{
    std::unique_lock<std::mutex> em(cerrojo);
    if(nh_actual > 1)
    {
        --nh_actual; 
        dormir.wait(em);
    }
    else
    {
        dormir.notify_all();
        //nh_actual = nh_maximo;
    }
}

void simBarrier::resetBarrier()
{
    std::unique_lock<std::mutex> em(cerrojo);
    nh_actual = nh_maximo;
    dormir.notify_all();
}

void ejecucionHebras(simBarrier& bar, std::mutex& cerrojo)
{
    cerrojo.lock();
    //printf("%d llegando a barrera...\n", std::this_thread::get_id());
    std::cout << std::this_thread::get_id() << " llegando a barrera...\n";
    cerrojo.unlock();
    bar.toWaitOnBarrier();
    cerrojo.lock();
    //printf("%d saliendo de barrera...\n", std::this_thread::get_id());
    std::cout << std::this_thread::get_id() << " saliendo de barrera...\n";
    cerrojo.unlock();
}
/**
 
 * @author Alan Thoams, Anish Salvi, Ninad Dehadrai, Rohan Ijare
 * @version 0.1
 * @date 2021-02-25
 *
 */
#include <stdio.h>

#include <stdlib.h>

#include <CL/cl.h>
#pragma warning(disable : 4996)


const char* OpenCLSource[] = {

"__kernel void DistanceConvert(__global float* c, __global float* a,__global float* b)",

"{",

" // Index of the elements to add \n",

" unsigned int n = get_global_id(0);",

" // Distance conversion \n",

" c[n] = a[n] / b[n];",


"}"



};

// Some interesting data for the vectors

float rawInputValue[100] = {  500,652,446,8816,865,485,789,932,879,865,
                              8926,487,635,459,333,5278,111,305,895,7916,
                              1210,462,856,8165,485,789,9612,859,867,
                              8516,5287,325,459,333,548,111,305,895,796,
                              500,152,462,856,895,4815,7892,932,879,8157,
                              896,587,625,5189,333,578,111,315,895,796,
                              5100,150,462,856,865,485,789,962,879,857,
                              956,5247,625,459,333,578,111,3105,895,789,
                              500,150,4612,8526,895,485,789,932,879,867,
                              896,587,635,489,333,547,111,3215,895,7816,7819
};

float conversionValue[1] = { 100 };
// Number of input taken from sensor

#define SIZE 100

// Main function

float main(int argc, char** argv)

{

    //Vector in host Memory

    float rawSignalArray[SIZE], factor[SIZE];

    float signalArray[SIZE];

    // Initialize with some interesting repeating data

    for (int c = 0; c < SIZE; c++)

    {

        rawSignalArray[c] = rawInputValue[c % 100];

        factor[c] = conversionValue[c % 1];

        signalArray[c] = 0;

    }

    //Get an OpenCL platform

    cl_platform_id cpPlatform;

    clGetPlatformIDs(1, &cpPlatform, NULL);

    // Get a GPU device

    cl_device_id cdDevice;

    clGetDeviceIDs(cpPlatform, CL_DEVICE_TYPE_GPU, 1, &cdDevice, NULL);

    char cBuffer[1024];

    clGetDeviceInfo(cdDevice, CL_DEVICE_NAME, sizeof(cBuffer), &cBuffer, NULL);

    printf("CL_DEVICE_NAME: %s\n", cBuffer);

    clGetDeviceInfo(cdDevice, CL_DRIVER_VERSION, sizeof(cBuffer), &cBuffer, NULL);

    printf("CL_DRIVER_VERSION: %s\n\n", cBuffer);

    // Create a context to run OpenCL enabled GPU

    cl_context GPUContext = clCreateContextFromType(0, CL_DEVICE_TYPE_GPU, NULL, NULL, NULL);

    // Create a command-queue on the GPU device

    cl_command_queue cqCommandQueue = clCreateCommandQueue(GPUContext, cdDevice, 0, NULL);

    // Allocate GPU memory for source vectors AND initialize from CPU memory

    cl_mem GPUVector1 = clCreateBuffer(GPUContext, CL_MEM_READ_ONLY |

        CL_MEM_COPY_HOST_PTR, sizeof(int) * SIZE, rawSignalArray, NULL);

    cl_mem GPUVector2 = clCreateBuffer(GPUContext, CL_MEM_READ_ONLY |

        CL_MEM_COPY_HOST_PTR, sizeof(int) * SIZE, factor, NULL);

    // Allocate output memory on GPU

    cl_mem GPUOutputVector = clCreateBuffer(GPUContext, CL_MEM_WRITE_ONLY,

        sizeof(int) * SIZE, NULL, NULL);

    // Create OpenCL program with source code

    cl_program OpenCLProgram = clCreateProgramWithSource(GPUContext, 7, OpenCLSource, NULL, NULL);

    // Build the program (OpenCL JIT compilation)

    clBuildProgram(OpenCLProgram, 0, NULL, NULL, NULL, NULL);

    // Create a handle to the compiled OpenCL function (Kernel)

    cl_kernel OpenCLObstacleDetect = clCreateKernel(OpenCLProgram, "DistanceConvert", NULL);

    // In the next step we associate the GPU memory with the Kernel arguments

    clSetKernelArg(OpenCLObstacleDetect, 0, sizeof(cl_mem), (void*)&GPUOutputVector);

    clSetKernelArg(OpenCLObstacleDetect, 1, sizeof(cl_mem), (void*)&GPUVector1);

    clSetKernelArg(OpenCLObstacleDetect, 2, sizeof(cl_mem), (void*)&GPUVector2);

    // Launch the Kernel on the GPU

    // This kernel only uses global data

    size_t WorkSize[1] = { SIZE }; // one dimensional Range

    clEnqueueNDRangeKernel(cqCommandQueue, OpenCLObstacleDetect, 1, NULL,

        WorkSize, NULL, 0, NULL, NULL);

    // Copy the output in GPU memory back to CPU memory

    clEnqueueReadBuffer(cqCommandQueue, GPUOutputVector, CL_TRUE, 0,

        SIZE * sizeof(int), signalArray, 0, NULL, NULL);

    // Cleanup

    clReleaseKernel(OpenCLObstacleDetect);

    clReleaseProgram(OpenCLProgram);

    clReleaseCommandQueue(cqCommandQueue);

    clReleaseContext(GPUContext);

    clReleaseMemObject(GPUVector1);

    clReleaseMemObject(GPUVector2);

    clReleaseMemObject(GPUOutputVector);

    printf("Distance Detection module in Truck Platooning system\n");


    printf("System output array to be fed into the distance module\n");

    for (int i = 0; i < SIZE; i++)
    {

        printf("[%f / %f = %f]\n", rawSignalArray[i], factor[i], signalArray[i]);

        if (signalArray[i] < 10) {
            printf("Distance less than 10m Obstacle detectd\n");
        }
        else
        {
            printf("No Obstcle\n");

        }
    }


    return 0;

}

/***********************************************************End Of File ******************************************************************************************/
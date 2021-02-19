/********************************************************************************************************************

Base Code : Obstacle Avoidance
@author   : Alan Thomas, Anish Salvi, ,Ninad Dehadrai , Rohan Ijhare

*********************************************************************************************************************/
#include <iostream>

using namespace std;
#define mileage 5
    int destinationDistanceLeft;
    int timeToDestination,distanceStart,distanceTravel,timeStart;
    int vehicleSpeed = 0;
    int steeringAngle = 0;
    int vGap = 2;
    static int array[10];

    void Server();
    void Client();
/********************************************************************************************************************

Main Function

*********************************************************************************************************************/

int main()
{
    int system =1;
    cout << "              <   <       <           < Truck Platooning System >          >       >   > "<< endl;
    cout << "              <   <       <           <         Welcome         >          >       >   > "<< endl<<endl;
    cout << "                                               Connected                                "<< endl<<endl;
    array[3]=1;
    while(system==1)
    {

    Server();
    Client();
    cout<<"                         To Stop the Truck Companion Mode press 0 and 1 to continue"<<endl;
    cin>>system;

    }

    return 0;
}

/*
Array Elements :
            0  : Speed of Leader Vehicle
            1  : Distance Left to Destination
            2  : Steering Angle
            3  : Platooning Information
            4   : Speed of follower
            5   : Distance left for follower including front vehcicle gap tp be kept plus front vehicle length
            6   : Steering Angle
            7   : Platooning Information
*/

/*
Leading Vehicle is Server Function

*/

/********************************************************************************************************************

Server Function / Front Vehicle

*********************************************************************************************************************/
    void Server()
    {

    int object=0;
    static int init =0;
    cout << "              <   <       <           < Lead Vehicle >          >       >   > "<< endl;
    if(array[1]!=0||init==0)
    {
    cout<<"                                           Obstacle Detection                            " <<endl;
    cout<<"1 - obstacle in Front  ||  2 - Car on Right   ||   3- Car on Left   ||     0 - no obstacle" <<endl;
    cin>>object;

    if(object!=0)
    {
        cout << "                                           Reduce Speed to 20                            " <<endl;
        array[0] = 20;
    }
    else
    {
    cout << "                         Enter the Vehicle Speed  (Max 80 for safety)" <<endl;
    cin>>vehicleSpeed;
    array[0] = vehicleSpeed;
    array[1] = array[1]-1;
    }


    if(init==0)
    {
    cout<<"                         Enter the Distance left to Destination"<<endl;
    cin>>destinationDistanceLeft;
    array[1]=destinationDistanceLeft;
    init=1;
    }
    else
    {


         cout<<"                         Distance left to Destination"<<array[1]<<"m"<<endl;
    }



    cout << "                         Enter the Steering Angle" <<endl;
    cin>>steeringAngle;
    array[2] = steeringAngle;

    cout<<"                          Speed"<<array[0]<< endl;
    cout<<"                          Distance left to Destination"<<array[1]<< endl;
    cout<<"                          SteeringAngle"<<array[2]<< endl;
    }
    else
    {
       array[3] = 1;

       //check if following trucks is in still in platoon
       if(array[7]==1)
       {
        cout<<"                         Companion Mode End"<<endl;
        cout<<"                         Destination Reached "<<endl;
       }
       else{
        cout<<"                         Companion Mode Ending Soon"<<endl;
        cout<<"                         Leader Destination Reached "<<endl;
       }
    }

    }


/********************************************************************************************************************

Client Function / Trailing Vehicle

*********************************************************************************************************************/

    void Client()
    {

    int object=0;
    static int init =0;
    if(array[5]!=0||init==0)
    {
    cout << "              <   <       <           < Following Vehcile >          >       >   > "<< endl;

    if(init==0)
    {
        //client distance to destination =  distance left for front truck + vgap + truck length;
        array[5] = array[1]+vGap+1;  //distance +1 length of truck
        init =1;
    }
    //clientSpeed = serverspeed;
    array[4] = array[0];//speed
    //clientSteering = serversteering angle
    array[6] = array[2];  //steering angle



    cout << "                                           Obstacle Detection     Client                        " <<endl;
    cout<<"                     1 - obstacle in Front    2 - Car on Right       3- Car on Left        0 - no obstacle" <<endl;
    cin>>object;

    if(object!=0)
    {
        cout << "                         Enter the Steering Angle" <<endl;
        cin>>steeringAngle;
        array[6] = steeringAngle;
        cout << "                                           Reduce Speed to 20                            " <<endl;
        array[4] = 20;
        //increase vehcile gap
        vGap = vGap + 1;
        //array[5] = array[5] + 1;
        cout<<"vehicle Gap"<<vGap<<endl;
        if(vGap>4)
        {
            cout<<"Platoon break"<<endl;
            array[7]=0;
        }
    }
    else
    {
        //if no object then reduce vehicle distance to destination
        array[5] = array[5] - 1;
        if(vGap>2&&array[4]>20)
        {
            vGap = vGap - 1;
            cout<<"Increase Vehicle Speed to platoon"<<endl<<vGap<<endl;
            array[4] = array[4] + 10; //increase speeed by 10 kmph        }
        else
        {
            cout<<"In Platoon"<<endl;
        }
    }


    cout<<"                          Speed"<<array[4]<< endl;
    cout<<"                          Distance left to Destination"<<array[5]<< endl;
    cout<<"                          SteeringAngle"<<array[6]<< endl;
    }
    else
    {
    //Follower Vehicle Reached Destination
    array[7]=1;
    }

    }

/*****************************************************end of file ************************************/

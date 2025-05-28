/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Project written by Jason Sybert, Sal Bonadonna, and Grant Gose
 * 
 */



package com.mycompany.secureprogrammingprojecttempsensor;

import java.util.Scanner;
import java.util.Random;
import java.util.concurrent.*;

public class SecureProgrammingProjectTempSensor 
{
    private static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static boolean isF = false;
    
    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);
        
        //regexes
        String numberRegex = "^-?\\d+(\\.\\d+)?$";
        String tempDefRegex = "^[CFcf]$";
        
        double minTemp;
        double maxTemp;
        String tempDef;
        
        while (true)
        {
            System.out.println("Enter the minimum temperature limit: ");
            String minTempInput = scanner.nextLine();
            if(minTempInput.matches(numberRegex))
            {
                minTemp = Double.parseDouble(minTempInput);
                break;
            }
            else
            {
                System.out.println("Invalid input. Please enter a valid number for the minimum temperature.");
            }
        }
        
        while (true)
        {
            System.out.println("Enter the maximum temperature limit: ");
            String maxTempInput = scanner.nextLine();
            if(maxTempInput.matches(numberRegex))
            {
                maxTemp = Double.parseDouble(maxTempInput);
                if(maxTemp >= minTemp)
                {
                    break;
                }
                else
                {
                    System.out.println("Maximum temperature cannot be lower than the minimum temperature. Please try again.");
                }
            }
            else
            {
                System.out.println("Invalid input. Please enter a valid number for the maximum temperature.");
            }
        }
        
        while(true)
        {
            System.out.println("Enter F for Fahrenheit or C for Celcius: ");
            tempDef = scanner.nextLine();
            if(tempDef.matches(tempDefRegex))
            {
                break;
            }
            else
            {
                System.out.println("Invalid input. Please enter 'F' or 'C'.");
            }
        }
        
        
        if(tempDef.toUpperCase().equals("F"))
        {
            minTemp = convertToCelsius(minTemp);
            maxTemp = convertToCelsius(maxTemp);
            isF = true;
        }
        else if(tempDef.toUpperCase().equals("C"))
        {
            isF = false;
        }
        
        double minTempFinal = minTemp;
        double maxTempFinal = maxTemp;
        
        scheduler.scheduleAtFixedRate(() -> checkTemperature(minTempFinal, maxTempFinal), 0, 10, TimeUnit.SECONDS);
        
        while(true)
        {
            System.out.println("Enter 'exit' to stop or press any key to check again.");
            String input = scanner.nextLine();
            
            if(input.equalsIgnoreCase("exit"))
            {
                System.out.println("Exiting temperature monitor...");
                scheduler.shutdown();
                break;
            }
            else
            {
                checkTemperature(minTempFinal, maxTempFinal);
            }
        }
        
        scanner.close();
    }
    
    private static void checkTemperature(double minTemp, double maxTemp)
    {
        double currentTempCelsius = getTemperatureFromSensor();
        
        if(isF)
        {
            double currentTempFahrenheit = convertToFahrenheit(currentTempCelsius);
            System.out.println("Current Temperature: " + currentTempFahrenheit + "F");
        }
        else
        {
            System.out.println("Current Temperature: " + currentTempCelsius + "F");
        }
        
        if(currentTempCelsius < minTemp)
        {
            System.out.println("Warning: Temperature is below the minimum limit!");
        }
        else if(currentTempCelsius > maxTemp)
        {
            System.out.println("Warning: Temperature is above the maximum limit!");
        }
        else
        {
            System.out.println("Temperature is within safe range");
        }
        
    }
    
    private static double getTemperatureFromSensor()
    {
        Random random = new Random();
        return -40 + (200 - (-40)) * random.nextDouble();
    }
    
    private static double convertToFahrenheit(double celsius)
    {
        return (celsius * 9/5)+32;
    }
    
    private static double convertToCelsius(double fahrenheit)
    {
        return (fahrenheit - 32) * 5/9;
    }
}

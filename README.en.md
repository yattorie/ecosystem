# Ecosystem simulator

## Project Author
### Orlov Andrei

## Description
This is an application for managing a virtual ecosystem where users can add plants and animals, set ecosystem parameters (temperature, humidity, amount of available water), predict changes in populations.

## Functionality

- Species management (add plants and animals, edit diet in animals, delete species).
- Each simulation is saved separately, the user can continue working with an existing one or create a new one.
- Simulation of interactions between species (food chains are taken into account).
- Population forecasting based on ecosystem parameters (temperature, humidity, amount of available water).
- All objects are stored in .txt files.
- A system of predicting which species populations will increase, decrease or remain stable depending on current conditions is implemented.

## Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/yattorie/ecosystem-simulator.git
   cd ecosystem
   ```

2. Build the project:
   ```sh
   mvn clean install
   ```

3. Run the application:
   ```sh
   mvn exec:java -Dexec.mainClass="com.orlovandrei.ecosystem.Application"
   ```

## Usage

- Launch the application.
- Choose the option: create a new ecosystem or load an existing one (existing ecosystems are located in the resource folder, new ecosystems are also saved there in a new directory with all information in .txt files).
- Manage your ecosystem: add plants and animals, edit parameters.
- View predictions of changes in populations based on current conditions.
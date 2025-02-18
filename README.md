# Magical Map 🗺✨  

## 📌 Project Overview  
This project is developed as part of the **CmpE 250: Data Structures and Algorithms** course at **Boğaziçi University (Fall 2025)**.  
It involves **graph traversal with dynamic constraints** in a magical land where the **Wizard of Oz** challenges visitors to navigate through an enchanted map.  

## 📖 Problem Statement  
The wizard has shattered the land into small squares and rearranged them. Visitors must follow the **wizard’s instructions** to reach specific **magical sites** while overcoming hidden obstacles and dynamically changing routes. The **fastest path** must be recalculated whenever new information is revealed.

### 🔍 Key Features:
- **Graph-Based Pathfinding**: Find the shortest path while navigating dynamically revealed obstacles.  
- **Line of Sight Mechanics**: Nodes reveal their true properties when close enough.  
- **Dynamic Route Recalculation**: If an obstacle appears, replan the path efficiently.  
- **Wizard’s Help System**: Certain checkpoints allow choosing which obstacles to remove for future paths.  
- **Deterministic Decisions**: At each step, only a unique fastest path is expected.  

## 📂 Input Files  
1. **Land File** – Defines the map as a grid with node types.  
2. **Travel Time File** – Specifies travel times between connected nodes.  
3. **Mission File** – Lists objectives, starting positions, and wizard's assistance points.  

## 📝 Output Format  
- Movement steps (`Moving to x-y`)  
- Path recalculation when encountering obstacles (`Path is impassable!`)  
- Objectives reached (`Objective n reached!`)  
- Wizard’s chosen assistance (`Number n is chosen!`)  

## 💻 Implementation Details  
- Implemented in **Java**  
- Uses **Graph Algorithms** (Dijkstra, A* for pathfinding)  
- Optimized **line-of-sight updates** using **Pythagorean distance**  
- Efficient **data structures** (hash tables, priority queues)  

## 🚀 Running the Project  
Compile and run with:  
```sh
javac *.java
java Main <land_file> <travel_time_file> <mission_file> <output_file>

# Domino Game

**Console Domino Game** - A four-player domino game featuring both human and CPU players, complete with configurable settings for a personalized experience. The game includes robust turn management, detailed play-by-play information, and comprehensive final summaries.

This project encompasses the entire process of gathering software requirements and designs, including the Software Requirements Specification (SRS) and Software Design Specification (SDS) found in the **System-Specification-and-Design-Models** directory. This includes:

- Gathering functional and non-functional requirements.
- Defining use cases along with a use case diagram that generalizes the interactions between the use case specifications.
- Outlining entities and responsibilities using CRC cards enhanced with UML notation.
- Finally, creating a class diagram based on the initial relationships drawn in the CRC cards while applying the architectural pattern of Model-View-Controller (MVC) and adhering to SOLID principles.

In the **Project-Documentation** section, you will find all the project documentation generated with JavaDoc. To view it, simply open `index.html` in your browser.

The **Project-Implementation** folder contains the Java implementation of the domino game. Additionally, it includes a Dockerfile that allows you to quickly see the project in action.

To deploy and play the game, use the following Docker commands from this directory:

```bash
docker build -t domino-game ./Project-Implementation/domino
docker run -it domino-game

# affinitas-filtering-matches
Affinitas Demo for Filtering Matches
[TASK README](https://github.com/affinitas/coding_exercises_options/blob/master/filtering_matches/README.md)

### Installation & Run Instructions
1. Fork or download the project
2. Import pom.xml as Maven Project and let Maven do its magic :)
3. Test the project by either:
    * running the _ReportIT_ class as JUnit application
    * with "mvn verify" command on the project folder
4. Run the project by either:
    * running the "main()" method of _FilteringMatchesApplication_ class
    * with "mvn spring-boot:run" command on the project folder
5. All functionality of the project resides within http://localhost:8080/report page and the project
has built-in Actuator service for querying the server status/info/path variables/... parameters under http://localhost:8080/ port.

_!!No additional setup is required except Maven!!_

---
###### Used Languages, Tools & Plugins:
+ Java 8, ReactJS, Babel, JQuery, Ajax, Bootstrap CSS, JSON, REST && TDD Methodology
---
+ Eclipse (IDE)
+ Maven (for project build and dependencies)
+ Spring Boot (for running the project and dependencies)
+ Spring Data JPA (for repository/service backend)
+ Spring Web (web project)
+ Spring Actuator (provides useful runtime project properties under various links)
+ Spring Test (has Mockito, JUnit, Spring Test modules for TDD)
+ Spring DevTools (removes the need to stop/restart project after changes; auto deploys and refreshes)
+ RestAssured (fantastic library for testing REST services and capabilities of project without configuration -- automatically default to
"...IT" named classes to find and run tests)
+ HSQLDB (in-memory database for backend repo/service capability)
+ Maven Failsafe Plugin (for integration tests -- both pre and post -- && produces test result documents automatically)

### Assumptions, Design Choices and Comments
> **COMMENTS** The task was really enjoyable and fun. I got to use the _TDD_ methodology on this project (as much as I can :)) and ensured that the project grew step-by-step, satisfying all previous conditions.
> Due to not being able to use it on a daily/work environment basis in my current situation, this was a really good opportunity.
> I have also used _ReactJs_ for the first time in any project and am actually quite interested in developing more hobby projects using it. 
> Although it is completely different in terms of thinking, design strategies and syntax, I have adapted quickly after checking up some examples and ~~hopefully~~ produced an efficient and easy to understand single page application.

> **DESIGN CHOICES & ASSUMPTIONS** Some of the filters threw me off in terms of logic. For example: the boolean filters.
> "In Contact" filter is asked to be implemented in a boolean manner, with true or false setting. 
> However, I would have actually designed it as a single choice filter between "Yes / No / Don't Care" to ensure that the user can search for matches who are "definitely in contact (previous connection) / definitely not in contact (someone new) / don't care whether new or previously met (either)".
> Apart from that, I have taken the liberty of designing the special cases of certain filters, namely >95 years for AgeFilter & >210cm for HeightFilter & <30km and >300km for DistanceFilter.
> As I said, some of them didn't make sense to me such as searching for someone older than 95 or someone who is far far away. While implementing them, I have made special non-intuitive design choices since I really don't like front end development and especially css and toggles/hide shows.
>  * For >95 age --> the user can input any number bigger than 95 to "min" field
>  * For >210 cm --> the user can input any number bigger than 210 to "min" field

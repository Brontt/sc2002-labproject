# UI Examples - Enhanced Filtering & Recommendation System

## Visual Examples of the New Student Menu

### 1. Main Menu (Default State)

```
╔════════════════════════════════════════════╗
║          STUDENT MENU                      ║
╚════════════════════════════════════════════╝
Status: Filter: OFF | Recommendations: OFF

1) View Internships
2) Configure Filters
3) Configure Recommendations
4) Toggle Filter Mode (Currently: OFF)
5) Toggle Recommendation Mode (Currently: OFF)
6) Clear All Filters & Settings
7) Apply for Internship
8) Show Application Status / Manage Offers
9) Change Password
0) Logout

Select option: _
```

### 2. Main Menu (Both Modes Active)

```
╔════════════════════════════════════════════╗
║          STUDENT MENU                      ║
╚════════════════════════════════════════════╝
Status: Filter: ON (status=null, level=null, major=Computer Science, company=null, keyword=software, levelPrefs=[INTERMEDIATE, ADVANCED]) | Recommendations: ON

1) View Internships
2) Configure Filters
3) Configure Recommendations
4) Toggle Filter Mode (Currently: ON)
5) Toggle Recommendation Mode (Currently: ON)
6) Clear All Filters & Settings
7) Apply for Internship
8) Show Application Status / Manage Offers
9) Change Password
0) Logout

Select option: _
```

### 3. Configure Filters Screen (Year 3+ Student)

```
╔════════════════════════════════════════════╗
║       CONFIGURE FILTERS                    ║
╚════════════════════════════════════════════╝

Enter keywords to search (title/company/description) [Enter to skip]: software

As a Year 3 student, you can choose preferred internship levels:
Select preferred internship levels (comma-separated, e.g., 1,2,3):
  1) BASIC
  2) INTERMEDIATE
  3) ADVANCED
  [Enter for all levels]
Your choice: 2,3

Only show internships matching your major (Computer Science)? (y/n): y

Filter by specific company [Enter to skip]: 

✓ Filter configuration saved!
Current filters: status=null, level=null, major=Computer Science, company=null, keyword=software, levelPrefs=[INTERMEDIATE, ADVANCED]

Reminder: Use option 4 to toggle filter mode ON/OFF

Press Enter to continue...
```

### 4. Configure Filters Screen (Year 1-2 Student)

```
╔════════════════════════════════════════════╗
║       CONFIGURE FILTERS                    ║
╚════════════════════════════════════════════╝

Enter keywords to search (title/company/description) [Enter to skip]: marketing

As a Year 2 student, you can only apply to BASIC level internships.

Only show internships matching your major (Computer Science)? (y/n): n

Filter by specific company [Enter to skip]: 

✓ Filter configuration saved!
Current filters: status=null, level=null, major=null, company=null, keyword=marketing, levelPrefs=[]

Reminder: Use option 4 to toggle filter mode ON/OFF

Press Enter to continue...
```

### 5. Configure Recommendations Screen

```
╔════════════════════════════════════════════╗
║    CONFIGURE RECOMMENDATION RANKING        ║
╚════════════════════════════════════════════╝

Keywords for ranking boost (title/company) [Enter to skip]: developer

Select levels to consider for ranking:
Select preferred internship levels (comma-separated, e.g., 1,2,3):
  1) BASIC
  2) INTERMEDIATE
  3) ADVANCED
  [Enter for all levels]
Your choice: 2,3

Prioritize internships matching your major? (y/n): y

Prioritize applications closing soon? (y/n): y

--- Set Ranking Priorities ---
Assign priority numbers (1 = highest priority)
Each criterion should have a unique priority number.
Priority for closingSoon: 1
Priority for levelFit: 3
Priority for preferredMajor: 2
Priority for keywords: 4

✓ Recommendation ranking configured!
Keywords: developer
Levels: [INTERMEDIATE, ADVANCED]
Major match: Yes
Closing soon: Yes
Priorities: {closingSoon=1, levelFit=3, preferredMajor=2, keywords=4}

Reminder: Use option 5 to toggle recommendation mode ON/OFF

Press Enter to continue...
```

### 6. Toggle Filter Mode

```
✓ Filter mode is now: ON

Press Enter to continue...
```

### 7. Toggle Recommendation Mode

```
✓ Recommendation mode is now: ON

Press Enter to continue...
```

### 8. View Internships (Filter OFF, Recommendations OFF)

```
╔════════════════════════════════════════════╗
║       AVAILABLE INTERNSHIPS                ║
╚════════════════════════════════════════════╝
Showing all eligible internships (8 total)
Sorted by: Title (alphabetical)

┌────┬──────────┬──────────────────────────┬────────────────────┬──────────────┬────────┬────────────┬───────┐
│No. │    ID    │          Title           │      Company       │    Level     │ Major  │Close Date  │ Slots │
├────┼──────────┼──────────────────────────┼────────────────────┼──────────────┼────────┼────────────┼───────┤
│  1 │ I1002    │ Data Analyst Intern      │ FinSight Analytics │ INTERMEDIATE │   IS   │ 2025-12-25 │     3 │
│  2 │ I1009    │ Frontend Developer       │ Appify Tech        │ INTERMEDIATE │   CS   │ 2025-12-19 │     4 │
│  3 │ I1004    │ Marketing Assistant      │ MarketWise         │    BASIC     │  BUS   │ 2025-11-30 │     6 │
│  4 │ I1005    │ Mobile App Developer     │ Appify Tech        │ INTERMEDIATE │   CS   │ 2025-12-22 │     5 │
│  5 │ I1007    │ Product Management Int.. │ ByteForge          │ INTERMEDIATE │  BUS   │ 2025-12-18 │     3 │
│  6 │ I1008    │ Research Assistant       │ NTU Research Lab   │  ADVANCED    │  MAE   │ 2025-12-28 │     2 │
│  7 │ I1001    │ Software Intern          │ ByteForge          │    BASIC     │   CS   │ 2025-12-20 │     5 │
│  8 │ I1006    │ Mechanical Engineer I... │ MechaCore          │  ADVANCED    │  MAE   │ 2025-12-10 │     2 │
└────┴──────────┴──────────────────────────┴────────────────────┴──────────────┴────────┴────────────┴───────┘

Press Enter to continue...
```

### 9. View Internships (Filter ON, Recommendations OFF)

```
╔════════════════════════════════════════════╗
║       AVAILABLE INTERNSHIPS                ║
╚════════════════════════════════════════════╝
Showing filtered results (3 of 8 eligible)
Sorted by: Title (alphabetical)

┌────┬──────────┬──────────────────────────┬────────────────────┬──────────────┬────────┬────────────┬───────┐
│No. │    ID    │          Title           │      Company       │    Level     │ Major  │Close Date  │ Slots │
├────┼──────────┼──────────────────────────┼────────────────────┼──────────────┼────────┼────────────┼───────┤
│  1 │ I1009    │ Frontend Developer       │ Appify Tech        │ INTERMEDIATE │   CS   │ 2025-12-19 │     4 │
│  2 │ I1005    │ Mobile App Developer     │ Appify Tech        │ INTERMEDIATE │   CS   │ 2025-12-22 │     5 │
│  3 │ I1001    │ Software Intern          │ ByteForge          │    BASIC     │   CS   │ 2025-12-20 │     5 │
└────┴──────────┴──────────────────────────┴────────────────────┴──────────────┴────────┴────────────┴───────┘
Note: Filtered by keyword="software", major="CS", levels=[INTERMEDIATE, ADVANCED]
But "Software Intern" is BASIC level - shown because it matches keyword

Press Enter to continue...
```

### 10. View Internships (Filter ON, Recommendations ON)

```
╔════════════════════════════════════════════╗
║       AVAILABLE INTERNSHIPS                ║
╚════════════════════════════════════════════╝
Showing filtered results (3 of 8 eligible)
Sorted by: Recommendation ranking

┌────┬──────────┬──────────────────────────┬────────────────────┬──────────────┬────────┬────────────┬───────┐
│No. │    ID    │          Title           │      Company       │    Level     │ Major  │Close Date  │ Slots │
├────┼──────────┼──────────────────────────┼────────────────────┼──────────────┼────────┼────────────┼───────┤
│  1 │ I1009    │ Frontend Developer       │ Appify Tech        │ INTERMEDIATE │   CS   │ 2025-12-19 │     4 │
│  2 │ I1005    │ Mobile App Developer     │ Appify Tech        │ INTERMEDIATE │   CS   │ 2025-12-22 │     5 │
│  3 │ I1001    │ Software Intern          │ ByteForge          │    BASIC     │   CS   │ 2025-12-20 │     5 │
└────┴──────────┴──────────────────────────┴────────────────────┴──────────────┴────────┴────────────┴───────┘
Ranked by: closingSoon (pri=1), major match (pri=2), level fit (pri=3), keywords (pri=4)
Top match: Frontend Developer (closes 2025-12-19, matches "developer", CS major, INTERMEDIATE level)

Press Enter to continue...
```

### 11. View Internships (No Results with Filter)

```
╔════════════════════════════════════════════╗
║       AVAILABLE INTERNSHIPS                ║
╚════════════════════════════════════════════╝

(No internships match your filter criteria.)
Show all eligible internships instead? (y/n): y

Showing all eligible internships (8 total)
Sorted by: Title (alphabetical)

[... full list displays ...]
```

### 12. Clear All Settings

```
Are you sure you want to clear all filters and settings? (y/n): y

✓ All filters and settings have been cleared.

Press Enter to continue...
```

### 13. Toggle Warning (No Config)

```
✓ Filter mode is now: ON
Note: No filters configured yet. Use option 2 to configure filters.

Press Enter to continue...
```

## Comparison: Before vs After

### Before (Old Menu)

```
=== Student Menu ===
1) View Available Internships
2) Set/Update Filters & Ranking (for recommendations)
3) Clear Filters
4) Apply for Internship
5) Show Internship Status / Manage Offers
6) Change Password
0) Logout
Select: _
```

**Issues**:
- Option 2 mixed filtering and ranking together
- No way to use filters without ranking
- No way to use ranking without filters
- No status display
- No toggle functionality
- Confusing UX

### After (New Menu)

```
╔════════════════════════════════════════════╗
║          STUDENT MENU                      ║
╚════════════════════════════════════════════╝
Status: Filter: OFF | Recommendations: OFF

1) View Internships
2) Configure Filters
3) Configure Recommendations
4) Toggle Filter Mode (Currently: OFF)
5) Toggle Recommendation Mode (Currently: OFF)
6) Clear All Filters & Settings
7) Apply for Internship
8) Show Application Status / Manage Offers
9) Change Password
0) Logout
Select option: _
```

**Improvements**:
- ✓ Separate filter and recommendation configuration
- ✓ Independent toggle switches
- ✓ Clear status display
- ✓ Visual box borders
- ✓ Better organization
- ✓ More intuitive workflow

## User Journey Examples

### Journey 1: Quick Browse (Default)
```
1. Login → Menu shows Filter: OFF, Recommendations: OFF
2. Select "1) View Internships"
3. See all eligible internships, alphabetically sorted
4. Select "7) Apply for Internship"
```

### Journey 2: Filtered Search
```
1. Login → Menu
2. Select "2) Configure Filters"
   - Enter keyword: "software"
   - Select major match: Yes
3. Select "4) Toggle Filter Mode" → Now ON
4. Select "1) View Internships"
5. See only CS software internships
6. Select "7) Apply for Internship"
```

### Journey 3: Get Recommendations
```
1. Login → Menu
2. Select "3) Configure Recommendations"
   - Set priorities: closingSoon=1, major=2
3. Select "5) Toggle Recommendation Mode" → Now ON
4. Select "1) View Internships"
5. See all internships ranked by your preferences
6. Top matches listed first
7. Select "7) Apply for Internship"
```

### Journey 4: Power User (Both Modes)
```
1. Login → Menu
2. Configure Filters (narrow down options)
3. Toggle Filter ON
4. Configure Recommendations (rank the filtered set)
5. Toggle Recommendations ON
6. View Internships → See best matches only!
7. Apply to top 3 internships
```

## Key UI Improvements

1. **Visual Hierarchy**: Box borders separate sections
2. **Status Display**: Always know current mode
3. **Toggle Labels**: Show current state (ON/OFF)
4. **Confirmation Messages**: Clear feedback after actions
5. **Helpful Prompts**: Guide users through configuration
6. **Error Handling**: Graceful fallbacks with user choice
7. **Consistency**: Similar patterns across all screens
8. **Accessibility**: Clear numbering and descriptions

# Enhanced Filtering and Recommendation System - User Guide

## Overview

The enhanced filtering and recommendation system provides students with powerful tools to find the most suitable internships. The system includes two main features:

1. **Filtering** - Show only internships that meet specific criteria
2. **Recommendations** - Rank internships based on personalized preferences

## Key Features

### 1. Year-Based Level Restrictions

**Automatic Enforcement:**
- **Year 1-2 students**: Can only view and apply to BASIC level internships
- **Year 3+ students**: Can view and apply to all levels (BASIC, INTERMEDIATE, ADVANCED)

This restriction is enforced automatically when viewing internships to ensure students only see opportunities they're eligible for.

### 2. Filter Configuration

Access via: **Menu Option 2 - Configure Filters**

Available filter options:
- **Keywords**: Search across title, company name, and description
- **Level Preferences** (Year 3+ only): Select specific internship levels
  - Can choose multiple levels (e.g., INTERMEDIATE and ADVANCED)
  - Leave blank to see all eligible levels
- **Major Matching**: Show only internships that match your major
- **Company Filter**: Filter by specific company name

**Note**: Configuring filters does NOT immediately apply them. Use the toggle (Option 4) to enable/disable filtering.

### 3. Recommendation Configuration

Access via: **Menu Option 3 - Configure Recommendations**

Customize how internships are ranked:
- **Keywords**: Boost internships matching specific keywords
- **Level Preferences**: Preferred internship levels for ranking
- **Major Prioritization**: Give higher rank to major-matching internships
- **Closing Soon**: Prioritize applications closing soon
- **Custom Priorities**: Assign priority numbers (1=highest) to each criterion
  - closingSoon
  - levelFit
  - preferredMajor
  - keywords

**Note**: Configuring recommendations does NOT immediately apply them. Use the toggle (Option 5) to enable/disable recommendation ranking.

### 4. Toggle Functions

#### Filter Toggle (Option 4)
- **OFF (default)**: Shows all eligible internships
- **ON**: Shows only internships matching your configured filters

If no internships match your filters, you'll be prompted to show all instead.

#### Recommendation Toggle (Option 5)
- **OFF (default)**: Internships sorted alphabetically by title
- **ON**: Internships ranked by your recommendation preferences

Best matches appear at the top of the list.

### 5. View Internships (Option 1)

This is the main view that applies your current settings:

**Display Logic:**
1. Start with all eligible internships (based on year and open status)
2. If Filter is ON → Apply configured filters
3. If Recommendations is ON → Rank by recommendation algorithm
4. If Recommendations is OFF → Sort alphabetically by title

**Status Bar**: Shows current filter and recommendation status at the top of the menu.

### 6. Clear All Settings (Option 6)

Resets everything:
- Clears all configured filters
- Clears all recommendation settings
- Turns OFF both filter and recommendation modes
- Resets ranking weights to defaults

## Usage Scenarios

### Scenario 1: Browse All Available Internships
1. Keep Filter OFF and Recommendations OFF (defaults)
2. Select "View Internships"
3. See all eligible internships in alphabetical order

### Scenario 2: Filter by Specific Criteria
1. Configure Filters (Option 2)
   - Example: Enter "software" as keyword, select CS major match
2. Toggle Filter ON (Option 4)
3. View Internships (Option 1)
4. See only software internships matching your major

### Scenario 3: Get Personalized Recommendations
1. Configure Recommendations (Option 3)
   - Set your priorities (e.g., closingSoon=1, preferredMajor=2, etc.)
2. Toggle Recommendations ON (Option 5)
3. View Internships (Option 1)
4. See internships ranked from best to worst match

### Scenario 4: Combined Filtering + Recommendations
1. Configure both Filters and Recommendations
2. Toggle both Filter ON and Recommendations ON
3. View Internships (Option 1)
4. See filtered internships, ranked by your preferences

## Tips

1. **Start Simple**: First try viewing all internships, then add filters gradually
2. **Year 3+ Students**: Take advantage of level preferences to focus on appropriate difficulty
3. **Closing Soon**: Enable "closing soon" priority if you want to catch urgent deadlines
4. **Keywords**: Use broad terms (e.g., "tech", "marketing") for better results
5. **Clear Often**: If you're not getting results, try clearing and reconfiguring

## Example Workflow for Year 3 CS Student

```
1. Login as Year 3 Computer Science student
2. Configure Filters:
   - Keywords: "software"
   - Level Preferences: INTERMEDIATE, ADVANCED
   - Major Match: Yes
3. Toggle Filter: ON
4. Configure Recommendations:
   - Keywords: "developer"
   - Prioritize major: Yes
   - Closing soon: Yes
   - Priorities: closingSoon=1, preferredMajor=2, levelFit=3, keywords=4
5. Toggle Recommendations: ON
6. View Internships
   → See CS-related software/developer internships, ranked by closing date and match quality
7. Apply for top matches!
```

## Technical Details

- **Filter Mode**: Uses boolean matching (AND logic for all criteria)
- **Recommendation Mode**: Uses weighted scoring algorithm with priority-based weights
- **Year Restrictions**: Enforced at both filter and eligibility levels
- **Persistence**: Settings persist within your session but reset when you logout

## Testing Credentials

For testing purposes:
- **Username**: U2310001A (Year 2 student) or U2310002B (Year 3 student)
- **Password**: password (default for all test accounts)

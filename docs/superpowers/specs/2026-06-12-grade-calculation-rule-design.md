# Grade Calculation Rule Refactor Design

## Goal

Refactor grade calculation so each course defines how usual score and final exam score are weighted into `total_score`, then converts that calculated total score into `grade_point` through the course-specific grade point ranges.

## Business Rules

- A grade entry accepts `student_id`, `course_id`, `usual_score`, `final_score`, and `remark`.
- `total_score` is calculated by the backend:
  - `total_score = usual_score * usual_weight / 100 + final_score * final_weight / 100`
- `grade_point` is calculated by matching `total_score` against the selected course's `course_grade_rules`.
- Each course stores its own `usual_weight` and `final_weight`.
- `usual_weight + final_weight` must equal `100`.
- Scores and weights must stay within `0-100`.
- Existing course-specific grade point ranges remain per-course and must cover `0-100` without overlap or gaps.

## Permissions

- Administrators can edit score weights and grade point ranges for every course.
- Teachers can edit score weights and grade point ranges only for their own courses.
- Students cannot edit grades or rules.

## Implementation Shape

- Add `usual_weight` and `final_weight` to `courses`.
- Remove `total_score` from required grade save input; preserve it as stored calculated output.
- Reuse the existing grade point range table.
- Extend the existing rules page into course grade calculation rules management.
- Add a teacher route to the same rules page, using backend visibility and ownership checks.

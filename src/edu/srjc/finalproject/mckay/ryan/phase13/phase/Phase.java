/*
    Ryan McKay
    McKayRyan821@yahoo.com
    20171229
    FinalProject_McKay_Ryan_Phase13
    CS 17.11

    This class represents a Phase. It has an
    arraylist of Challenge objects, representing
    this Phase's challenges. It also holds which
    Phase number it is, its start date, and if
    it has been completed.

    There is an arraylist of Phases in the Phase
    Scene that is used to represent the whole
    Phase 13 program. All the challenges for
    the current phase are grabbed from a phase
    object and placed into the main gridpane
    in the Phase Scene. These are then replaced
    with the new current Phase's challenges
    whenever one of the arrows in the top left
    or right are pressed.
*/

package edu.srjc.finalproject.mckay.ryan.phase13.phase;

import edu.srjc.finalproject.mckay.ryan.phase13.customalerts.DatabaseAccessError;
import edu.srjc.finalproject.mckay.ryan.phase13.phase.challenge.Challenge;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

public class Phase
{
    private int phaseNum = 0;
    private LocalDate phaseStartDate = null;
    private boolean phaseIsCompleted = false;

    private ArrayList<Challenge> phaseChallenges = new ArrayList<>();

    public Phase(ResultSet resultSet)
    {
        try
        {
            phaseNum = resultSet.getInt(1);
            String[] date = resultSet.getString(2).split("-");
            phaseStartDate = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
            phaseIsCompleted = resultSet.getBoolean(3);
        }
        catch (Exception e)
        {
            DatabaseAccessError databaseAccessError = new DatabaseAccessError();
        }
    }

    public int getPhaseNum()
    {
        return phaseNum;
    }

    public void setPhaseNum(int phaseNum)
    {
        this.phaseNum = phaseNum;
    }

    public LocalDate getPhaseStartDate()
    {
        return phaseStartDate;
    }

    public void setPhaseStartDate(LocalDate phaseStartDate)
    {
        this.phaseStartDate = phaseStartDate;
    }

    public boolean phaseIsCompleted()
    {
        return phaseIsCompleted;
    }

    public void setPhaseIsCompleted(boolean phaseIsCompleted)
    {
        this.phaseIsCompleted = phaseIsCompleted;
    }

    public ArrayList<Challenge> getPhaseChallenges()
    {
        return phaseChallenges;
    }
}

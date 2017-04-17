package br.ufpe.nti.model;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;

import javax.ejb.EJB;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.ufpe.nti.controller.repository.ClockHistoryRepository;
import factory.ClockFactory;
import factory.Clocks;

@RestController
public class ClockController {
	
	@EJB
	ClockHistoryRepository repo;
	
	@RequestMapping(value="/clock", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<Clocks> getClock(){

		Clocks clo =  ClockFactory.getClocks("Clocky");
		clo.setTime(LocalTime.now());
		clo.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		clo.setAngle(999.99f);
		
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<Clocks>(clo , httpHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value="/clock", method=RequestMethod.POST)
	public ResponseEntity<Clock> postClock(@RequestBody String hora){
		
		String[] horaQ = hora.split(":");
		
		int hours = Integer.parseInt(horaQ[1].trim().substring(1, 3));
		int minutes = Integer.parseInt(horaQ[2].trim().substring(0, 2));
		float angle = getAngle(hours, minutes);
				
		LocalTime time = LocalTime.of(hours, minutes);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		Clock clk = new Clock();
		clk.setTime(time);
		clk.setCreatedAt(timestamp);
		clk.setAngle(angle);
		
		repo.save(clk);
		
		return new ResponseEntity<Clock>(clk, HttpStatus.OK);
	}
	
	@RequestMapping(value="/clockhistory", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<List<Clock>> getClockHistory(){
		
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		List<Clock> list = repo.listAll();
		
		return new ResponseEntity<List<Clock>>(list, httpHeaders, HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/clockhistory/{idHistoryClock}", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<Clock> getClockById(@PathVariable long idHistoryClock){
		
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		List<Clock> list = repo.listAll();
		
		Clock response = null;
		
		for (int i = 0; i < list.size(); i++){
			Clock currClock = list.get(i);
			if (currClock.getId() == idHistoryClock){
				response = currClock;
				break;
			}
		}
		
		if (response == null){
			return new ResponseEntity<Clock>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Clock>(response, httpHeaders, HttpStatus.OK);
	}
	
	private float getAngle(int hours, int minutes){
		float hAngle = ((60 * hours) + minutes)/2;
		float mAngle = 6 * minutes; 
		float ans = Math.abs(hAngle - mAngle);
		if (ans > 180.0f){
			ans = 360.0f - ans;
		}
		return ans;
	}

}
